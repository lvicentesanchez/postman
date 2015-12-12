package io.github.lvicentesanchez.cluster.api

import akka.actor.ActorRef
import akka.pattern.ask
import io.github.lvicentesanchez.cluster.Application

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

trait ApplicationAPI {
  def createApplication(name: String): Future[Unit]
  def getApplicationEndpoint(name: String): Future[String]
}

class ApplicationAPIImpl(applicationRef: ActorRef, timeout: FiniteDuration) extends ApplicationAPI {
  import Application._

  override def createApplication(name: String): Future[Unit] =
    ask(applicationRef, Protocol.CreateApplication(name))(timeout).mapTo[Unit]

  override def getApplicationEndpoint(name: String): Future[String] =
    ask(applicationRef, Protocol.GetApplicationEndpoint(name))(timeout).mapTo[String]
}

object ApplicationAPIImpl {
  def apply(applicationRef: ActorRef, timeout: FiniteDuration): ApplicationAPI =
    new ApplicationAPIImpl(applicationRef, timeout)
}
