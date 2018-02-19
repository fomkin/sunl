package sunl

import java.nio.ByteBuffer

trait Bytes[T] {
  def empty: T
  def fromByteArray(bytes: Array[Byte]): T
  def fromByteBuffer(buffer: ByteBuffer): T
  def toByteArray(bytes: T): Array[Byte]
  def toByteBuffer(bytes: T): ByteBuffer
  // TODO write with offset
  def get(bytes: T, i: Int): Byte
  def concat(left: T, right: T): T
}

object Bytes {

  def apply[T: Bytes]: Bytes[T] = implicitly[Bytes[T]]

  final class BytesOps[T](bytes: T)(implicit instance: Bytes[T]) {
    def apply(i: Int): Byte = instance.get(bytes, i)
    def concat(right: T): T = instance.concat(bytes, right)
  }
}