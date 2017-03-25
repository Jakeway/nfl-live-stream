package models

case class Play(
  sp: Int,
  qtr: Int,
  down: Int,
  time: Option[String],
  yrdln: Option[String],
  ydstogo: Int,
  ydsnet: Int,
  posteam: Option[String],
  desc: Option[String],
  note: Option[String],
  players: Map[String, Vector[PlaySequence]]
)
