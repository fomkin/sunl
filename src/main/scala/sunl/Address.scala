package sunl

sealed trait Address

object Address {
  case class IPv4(b1: Byte, b2: Byte, b3: Byte, b4: Byte) extends Address
}
