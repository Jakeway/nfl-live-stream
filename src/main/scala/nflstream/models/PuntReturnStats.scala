package nflstream.models

case class PuntReturnStats(
  name: Option[String],
  ret: Int,
  avg: Int,
  tds: Int,
  lng: Int,
  lngtd: Int
)
