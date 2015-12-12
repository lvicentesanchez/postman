package io.github.lvicentesanchez.cluster.api

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import io.github.lvicentesanchez.cluster.User
import io.github.lvicentesanchez.data.{ Content, UserID }

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

trait UserAPI {
  def sendMessage(userID: UserID, content: Content): Future[Unit]
}

class UserAPIImpl(userRef: ActorRef, timeout: FiniteDuration) extends UserAPI {
  implicit val t: Timeout = timeout

  override def sendMessage(userID: UserID, content: Content): Future[Unit] =
    (userRef ? User.Protocol.SendMessage(userID, content)).mapTo[Unit]
}

object UserAPIImpl {
  def apply(userRef: ActorRef, timeout: FiniteDuration): UserAPI =
    new UserAPIImpl(userRef, timeout)
}
