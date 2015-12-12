package io.github.lvicentesanchez.actors

import akka.actor.SupervisorStrategy.Stop
import akka.actor.{ ReceiveTimeout, Actor, Props }
import akka.cluster.sharding.ShardRegion
import akka.cluster.sharding.ShardRegion.ExtractEntityId
import io.github.lvicentesanchez.actors.sharding.Blueprint
import scala.concurrent.duration._

class User() extends Actor {
  import User._

  context.setReceiveTimeout(10.seconds)

  override val receive: Receive = {
    case msg: SendMessage =>
      println(s"Actor ${context.self.path.name} received $msg")
    case ReceiveTimeout =>
      context.parent ! ShardRegion.Passivate(stopMessage = Stop)
    case Stop =>
      println(s"Actor ${context.self.path.name} is stopping!")

      context.stop(self)
    case _ => ()
  }
}

object User {

  case class SendMessage(userID: String, content: String)

  object Blueprint extends Blueprint {
    override val extractID: ExtractEntityId = {
      case msg @ SendMessage(id, _) => (id, msg)
    }

    override val name: String = "User"

    override val props: Props = Props(new User())
  }
}
