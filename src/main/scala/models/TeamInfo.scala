package models

case class TeamInfo(
  score: Map[String, Int],
  abbr: Option[String],
  to: Int,
  stats: Stats,
  players: Option[String]
)
