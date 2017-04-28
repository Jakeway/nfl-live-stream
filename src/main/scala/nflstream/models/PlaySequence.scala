package nflstream.models

case class PlaySequence(
  sequence: Int,
  clubcode: Option[String],
  playerName: Option[String],
  statId: Int,
  yards: Int
)
