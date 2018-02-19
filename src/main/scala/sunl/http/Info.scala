package sunl.http

case class Info(
  method: Method,
  version: Version = Version.`1.1`,
  path: Seq[String] = Nil,
  headers: Seq[(String, String)] = Nil
)
