package sunl

import scala.language.higherKinds

trait Pulling[P[_[+_], +_]] {
  def pure[IO[+_], A](x: => A): P[IO, A]
  def concat[IO[+_], A](left: P[IO, A], right: P[IO, A]): P[IO, A]
  def pull[IO[+_], A](p: P[IO, A]): IO[Option[A]]
}

object Pulling {

  def apply[P[_[+ _], + _]: Pulling]: Pulling[P] =
    implicitly[Pulling[P]]

  final class PullingOps[P[_[+_], +_], IO[+_]: Async, A](val p: P[IO, A])(implicit i: Pulling[P]) {
    def concat(right: P[IO, A]): P[IO, A] = i.concat(p, right)
    def pull(): IO[Option[A]] = i.pull(p)
  }
}
