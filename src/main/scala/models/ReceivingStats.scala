package models

case class ReceivingStats(
  name: Option[String],
  rec: Int,
  yds: Int,
  tds: Int,
  lng: Int,
  twopta: Int,
  twoptm: Int
)
