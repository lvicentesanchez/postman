package io.github.lvicentesanchez.cluster.api

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import io.github.lvicentesanchez.cluster.Application

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

trait ApplicationAPI {
  def createApplication(name: String): Future[Unit]
  def getApplicationEndpoint(name: String): Future[String]
}

class ApplicationAPIImpl(applicationRef: ActorRef, timeout: FiniteDuration) extends ApplicationAPI {
  implicit val t: Timeout = timeout

  override def createApplication(name: String): Future[Unit] =
    (applicationRef ? Application.Protocol.CreateApplication(name)).mapTo[Unit]

  override def getApplicationEndpoint(name: String): Future[String] =
    (applicationRef ? Application.Protocol.GetApplicationEndpoint(name)).mapTo[String]
}

object ApplicationAPIImpl {
  def apply(applicationRef: ActorRef, timeout: FiniteDuration): ApplicationAPI =
    new ApplicationAPIImpl(applicationRef, timeout)
}
