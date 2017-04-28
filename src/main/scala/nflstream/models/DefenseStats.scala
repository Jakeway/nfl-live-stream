package nflstream.models

case class DefenseStats(
  name: Option[String],
  tkl: Int,
  ast: Int,
  sk: Double,
  int: Int,
  ffum: Int
)

