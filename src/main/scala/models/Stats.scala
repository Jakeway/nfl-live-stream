package models

case class Stats(
  passing: Option[Map[String, PassingStats]],
  rushing: Option[Map[String, RushingStats]],
  receiving: Option[Map[String, ReceivingStats]],
  fumbles: Option[Map[String, FumbleStats]],
  kicking: Option[Map[String, KickingStats]],
  punting: Option[Map[String, PuntingStats]],
  kickret: Option[Map[String, KickReturnStats]],
  puntret: Option[Map[String, PuntReturnStats]],
  defense: Option[Map[String, DefenseStats]],
  team: TeamStats
)
