package nflstream.models

case class TeamStats(
  totfd: Int,
  totyds: Int,
  pyds: Int,
  ryds: Int,
  pen: Int,
  penyds: Int,
  trnovr: Int,
  pt: Int,
  ptyds: Int,
  ptavg: Int,
  top: Option[String]
)
