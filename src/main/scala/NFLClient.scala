import io.circe.Json
import models.{Drive, GameInfo, Play, Update}
import org.http4s.client.blaze.PooledHttp1Client
import io.circe.generic.auto._, io.circe.parser._


import scala.xml.XML

class NFLClient {

  private val nflUpdateURL = "http://www.nfl.com/liveupdate/game-center/%s/%s_gtd.json"
  private val thisWeeksScheduleURL = "http://www.nfl.com/liveupdate/scorestrip/ss.xml"
  private val gameOver = List("F", "FO")


  def decodeUpdate(gameUpdate: String): Either[String, Update] = {
    parse(gameUpdate) match {
      case Left(f) => Left(f.message)
      case Right(json) =>

        val jsonWithRootFieldIgnored = json.cursor.fields.flatMap { fields =>
          json.hcursor.downField(fields.head).focus
        }.getOrElse(Json.Null)

        // Remove the 'crntdrv' field to make parsing the nfl update json easier.
        val crntdrvRemoved = jsonWithRootFieldIgnored
          .hcursor.downField("drives").downField("crntdrv")
          .delete.top.getOrElse(Json.Null)

        crntdrvRemoved.as[Update] match {
          case Right(u) => Right(u)
          case Left(failure) =>
            Left(failure.message)
        }
    }
  }

  def gameUpdate(gameId: String): Either[String, Update] = {
    val httpClient = PooledHttp1Client()
    val nflDataURL = nflUpdateURL.format(gameId, gameId)
    val rawNFLData = httpClient.expect[String](nflDataURL)
    val parsedNflDataTask = rawNFLData map decodeUpdate
    val update = parsedNflDataTask.run
    httpClient.shutdownNow()
    update
  }

  def findChanges(oldU: Update, newU: Update): List[Play] = {

    val oldLastDriveNum = oldU.drives.keySet.toList.map(_.toInt).max
    val oldLastDrive = oldU.drives(oldLastDriveNum.toString)

    val newCurrentDriveNum = newU.drives.keySet.toList.map(_.toInt).max

    // if the old last drive and the new current drive are same,
    // need to just find out what play we left off at and fill in missing plays
    // if not, then find out where we left off in the old last drive, and fill in missing plays from missing drives

    // lastKnownDrive is the most recent drive known in the oldU update, but with up to date data from the newU update.
    val lastKnownDrive = newU.drives(oldLastDriveNum.toString)
    val lastKnownDriveNumPlays = lastKnownDrive.numplays
    val missedPlays = lastKnownDriveNumPlays - oldLastDrive.numplays
    val missingDrives = newCurrentDriveNum - oldLastDriveNum

    // gets the last n plays from the map of plays
    // the reason we do this complicated form of extracting plays is because we don't actually know
    // what the keys are in our playMap. all we know is that the most recent play will have the highest key in the map
    def lastNPlaysFromDrive(n: Int, playMap: Map[String, Play], acc: List[Play]): List[Play] = {
      if (n == 0) acc
      else {
        val maxKey = playMap.keySet.toList.map(_.toInt).max.toString
        val lastPlay = playMap(maxKey)
        lastNPlaysFromDrive(n - 1, playMap - maxKey, lastPlay :: acc)
      }
    }

    // get all the plays from the last n drives in the map of drives
    // set n = 1 to get the last drive
    def lastNDrives(n: Int, drives: Map[String,Drive], numDrives: Int, acc: List[Play]): List[Play] = {
      if (n == 0) acc
      else {
        val driveNum = numDrives - n + 1
        val drive = drives(driveNum.toString)
        val missedPlays = lastNPlaysFromDrive(drive.numplays, drive.plays, List())
        lastNDrives(n - 1, drives, numDrives, acc ++ missedPlays)
      }
    }

    // we combine the missed plays from the last known drive we were on, plus all plays on all drives we missed
    val lastKnownDriveMissedPlays = lastNPlaysFromDrive(missedPlays, lastKnownDrive.plays, List())
    lastNDrives(missingDrives, newU.drives, newCurrentDriveNum, lastKnownDriveMissedPlays)
  }

  def gameOver(info: GameInfo): Boolean = gameOver.contains(info.quarter)
  def gameHasntStarted(info: GameInfo): Boolean = info.quarter.equals("P")
  def gameInProgress(info: GameInfo): Boolean = !gameHasntStarted(info) && !gameOver(info)

  // get updates for the games currently underway
  def currentGameUpdates(games: Seq[GameInfo]): Seq[Either[String, Update]] =
    (games filter gameInProgress).map(game => gameUpdate(game.eid))

  def weeklyGameInfo: Seq[GameInfo] = {
    val httpClient = PooledHttp1Client()
    val gamesXML = httpClient.expect[String](thisWeeksScheduleURL)
    val gameInfoTask = gamesXML.map(str => {
      val gameXML = XML.loadString(str)
      val week = (gameXML \ "gms" \ "@w").toString
      for {
        games <- gameXML \ "gms"
        game <- games \ "g"
        eid <- game \ "@eid"
        day <- game \ "@d"
        time <- game \ "@t"
        quarter <- game \ "@q"
      } yield GameInfo(week, eid.toString, day.toString, time.toString, quarter.toString)
    })
    val gameInfo = gameInfoTask.run
    httpClient.shutdownNow()
    gameInfo
  }
}
