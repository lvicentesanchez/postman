package io.github.lvicentesanchez.cluster

import java.util.UUID

import akka.actor.{ Actor, Props, ReceiveTimeout, SupervisorStrategy }
import akka.cluster.sharding.ShardRegion
import akka.cluster.sharding.ShardRegion.ExtractEntityId
import io.github.lvicentesanchez.cluster.sharding.Blueprint

import scala.concurrent.duration._

class Application() extends Actor {
  import Application.Protocol

  context.setReceiveTimeout(10.seconds)

  override def receive: Receive = loop(None)

  def loop(endpoint: Option[String]): Receive = {
    case msg: Protocol.CreateApplication =>
      val uuid: String = UUID.randomUUID().toString
      context.become(loop(Option(uuid)))
      sender() ! (())

    case msg: Protocol.GetApplicationEndpoint =>
      sender() ! endpoint.fold("not found")(identity)

    case ReceiveTimeout =>
      context.parent ! ShardRegion.Passivate(stopMessage = SupervisorStrategy.Stop)

    case SupervisorStrategy.Stop =>
      context.stop(self)
  }
}

object Application {

  object Protocol {
    final case class CreateApplication(name: String) extends Command
    final case class GetApplicationEndpoint(name: String) extends Command
  }

  object Blueprint extends Blueprint {
    import Application.Protocol._

    override val extractID: ExtractEntityId = {
      case msg: CreateApplication => (msg.name, msg)
      case msg: GetApplicationEndpoint => (msg.name, msg)
    }

    override val name: String = "Application"

    override val props: Props = Props(new Application())
  }
}
