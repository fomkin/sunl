package sunl.http

import sunl._
import sunl.tcp.Tcp

import scala.language.higherKinds

final class Http[IO[+_]: Async, T: Bytes, P[_[+_], +_]: Pulling](tcp: Tcp[IO, T, P]) {

  private val pulling = Pulling[P]
  private val async = Async[IO]
  private val bytes = Bytes[T]

  case class Entity(info: Info, body: Body)

  type Request = Entity
  type Response = Entity

  sealed trait Body {
    def p: P[IO, T]
  }

  object Body {
    case class Chunked(p: P[IO, T]) extends Body
    case class Stream(p: P[IO, T]) extends Body
  }

  def server(address: Address, port: Short, handler: Request => IO[Response]): IO[Shutdown] =
    tcp.server(address, port) { (_, incoming) =>
      // TODO keep alive support
      def loop(buffer: T): IO[Option[P[IO, T]]] = {
        incoming.pull().flatMap {
          case Some(data) =>
            readInfo(buffer.concat(data)) match {
              case Right(requestInfo) =>
                // TODO detect chunked
                handler(Entity(requestInfo, Body.Stream(incoming))) map {
                  case Entity(responseInfo, responseBody) =>
                    Some(pulling.pure[IO, T](printInfo(responseInfo))
                      .concat(responseBody.p))
                }
              case Left(incomplete) => loop(incomplete)
            }
          case None =>
            async.pure(Option.empty[P[IO, T]])
        }
      }
      loop(bytes.empty)
    }

  def request(address: Address, port: Short, request: Request): IO[Response] = ???

  def readInfo(bytes: T): Either[T, Info] = ???

  def printInfo(info: Info): T = ???
}
