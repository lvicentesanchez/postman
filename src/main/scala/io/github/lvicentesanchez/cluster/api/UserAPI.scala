package io.github.lvicentesanchez.cluster.api

import akka.actor.{ Actor, ActorRef }
import akka.pattern.ask
import io.github.lvicentesanchez.cluster.User
import io.github.lvicentesanchez.data.{ Content, UserID }

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

trait UserAPI {
  def sendMessage(userID: UserID, content: Content): Future[Unit]
}

class UserAPIImpl(userRef: ActorRef, timeout: FiniteDuration) extends UserAPI {

  override def sendMessage(userID: UserID, content: Content): Future[Unit] =
    userRef.ask(User.Protocol.SendMessage(userID, content))(timeout, Actor.noSender).mapTo[Unit]
}

object UserAPIImpl {
  def apply(userRef: ActorRef, timeout: FiniteDuration): UserAPI =
    new UserAPIImpl(userRef, timeout)
}
