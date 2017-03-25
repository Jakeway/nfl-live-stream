package models

case class Score (
  `type`: Option[String],
  desc: Option[String],
  qtr: Int,
  team: Option[String],
  players: Map[String, String]
)
