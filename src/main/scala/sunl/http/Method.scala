package sunl.http

sealed trait Method

object Method {
  case object Post extends Method
  case object Put extends Method
  case object Delete extends Method
  case object Get extends Method
}
