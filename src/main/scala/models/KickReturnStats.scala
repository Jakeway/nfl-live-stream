package models

case class KickReturnStats(
  name: Option[String],
  ret: Int,
  avg: Int,
  tds: Int,
  lng: Int,
  lngtd: Int
)
