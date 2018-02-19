import sunl.Async.AsyncOps
import sunl.Bytes.BytesOps
import sunl.Pulling.PullingOps

import scala.language.{higherKinds, implicitConversions}

package object sunl {

  type Shutdown = () => Unit
  type Peer = Address

  implicit def toPullingOps[P[_[+_], +_]: Pulling, IO[+_]: Async, T](p: P[IO, T]): PullingOps[P, IO, T] =
    new PullingOps[P, IO, T](p)

  implicit def toAsyncOps[IO[+_]: Async, A](m: IO[A]): AsyncOps[IO, A] =
    new AsyncOps[IO, A](m)

  implicit def toBytesOps[T: Bytes](b: T): BytesOps[T] =
    new BytesOps[T](b)
}
