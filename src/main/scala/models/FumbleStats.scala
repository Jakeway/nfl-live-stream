package models

case class FumbleStats(
  name: Option[String],
  tot: Int,
  rcv: Int,
  trcv: Int,
  yds: Int,
  lost: Int
)
