package nflstream.models

case class Update (
  home: TeamInfo,
  away: TeamInfo,
  drives: Map[String,Drive],
  scrsummary: Map[String, Score],
  weather: Option[String],
  media: Option[String],
  yl: Option[String],
  qtr: Option[String],
  note: Option[String],
  down: Option[Int],
  togo: Int,
  redzone: Boolean,
  clock: Option[String],
  posteam: Option[String],
  stadium: Option[String]
)