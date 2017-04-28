package nflstream.obs

import nflstream.models.{Play, Update}
import monix.reactive._
import nflstream.UpdateE
import nflstream.client.NFLClient

import scala.concurrent.duration._
import scala.collection.{mutable, _}
import scalaz.concurrent.Task


class NFLObservable {

  private val client = new NFLClient
  private val weeklyGameInfo = client.weeklyGameInfo.run
  private def gameUpdateEithers(): Task[Seq[UpdateE]] = client.currentGameUpdates(weeklyGameInfo)
  private val updatesMap: Map[String, Update] = new mutable.HashMap[String,Update]()


  def nflDataSteamWithDelay(delay: Int): Observable[Seq[(String, Seq[Play])]] = {
    Observable.interval(delay.seconds)
      .map(_ => {
        val changes = for {
          updateE <- gameUpdateEithers().run
          (gameID, newUpdate) <- updateE.fold(_ => None, Some(_))
          oldUpdate <- updatesMap get gameID
        } yield (gameID, client.findChanges(oldUpdate, newUpdate))
        changes
      })
  }

  val nflData: Observable[Seq[(String, Seq[Play])]] =
    Observable.interval(2.seconds)
      .map(_ => {
        val changes = for {
          updateE <- gameUpdateEithers().run
          (gameID, newUpdate) <- updateE.fold(_ => None, Some(_))
          oldUpdate <- updatesMap get gameID
        } yield (gameID, client.findChanges(oldUpdate, newUpdate))
        changes
    })

}