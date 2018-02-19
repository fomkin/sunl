package sunl

import scala.language.higherKinds

trait Async[IO[+_]] {
  def pure[A](x: => A): IO[A]
  def map[A, B](m: IO[A])(f: A => B): IO[B]
  def flatMap[A, B](m: IO[A])(f: A => IO[B]): IO[B]
  def run[A](m: IO[A]): A
}

object Async {

  def apply[IO[+_]: Async]: Async[IO] = implicitly[Async[IO]]

  final class AsyncOps[IO[+_], A](m: IO[A])(implicit i: Async[IO]) {
    def map[B](f: A => B): IO[B] = i.map[A, B](m)(f)
    def flatMap[B](f: A => IO[B]): IO[B] = i.flatMap[A, B](m)(f)
    def run(): A = i.run(m)
  }
}