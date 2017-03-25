package models

case class PuntingStats(
  name: Option[String],
  pts: Int,
  yds: Int,
  avg: Int,
  i20: Int,
  lng: Int
)
