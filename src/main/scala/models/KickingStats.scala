package models

case class KickingStats(
  name: Option[String],
  fgm: Int,
  fga: Int,
  fgyds: Int,
  totpfg: Int,
  xpmade: Int,
  xpmissed: Int,
  xpa: Int,
  xpb: Int,
  xptot: Int
)
