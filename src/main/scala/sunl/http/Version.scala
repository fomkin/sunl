package sunl.http

sealed trait Version

object Version {
  case object `1.0` extends Version
  case object `1.1` extends Version
}