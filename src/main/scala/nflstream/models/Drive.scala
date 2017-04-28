package nflstream.models

case class Drive(
                  posteam: Option[String],
                  qtr: Int,
                  redzone: Boolean,
                  plays: Map[String, Play],
                  fds: Int,
                  result: Option[String],
                  penyds: Int,
                  ydsgained: Int,
                  numplays: Int,
                  postime: Option[String],
                  start: DriveInfo,
                  end: DriveInfo
)

