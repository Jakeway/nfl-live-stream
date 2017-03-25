package models

case class DriveInfo(
  qtr: Int,
  time: Option[String],
  yrdln: Option[String],
  team: Option[String]
)
