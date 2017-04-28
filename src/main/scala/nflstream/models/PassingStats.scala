package nflstream.models

case class PassingStats(
  name: Option[String],
  att: Int,
  cmp: Int,
  yds: Int,
  tds: Int,
  ints: Int,
  twopta: Int,
  twoptm: Int
)
