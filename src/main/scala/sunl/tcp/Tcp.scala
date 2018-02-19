package sunl.tcp

import sunl.{Address, Async, Bytes, Peer, Pulling, Shutdown}

import scala.language.higherKinds

abstract class Tcp[IO[+_]: Async, T: Bytes, P[_[+_], +_]: Pulling]() {

  type Incoming = P[IO, T]
  type Outgoing = IO[Option[P[IO, T]]]
  type OnNewConnection = (Peer, Incoming) => Outgoing
  type OnConnect = Incoming => Outgoing

  def server(address: Address, port: Short)(handler: OnNewConnection): IO[Shutdown]

  def client(address: Address, port: Short)(handler: OnConnect): IO[Shutdown]
}
