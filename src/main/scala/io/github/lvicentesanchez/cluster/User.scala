package io.github.lvicentesanchez.cluster

import akka.actor.{ Actor, Props, ReceiveTimeout, SupervisorStrategy }
import akka.cluster.sharding.ShardRegion
import akka.cluster.sharding.ShardRegion.ExtractEntityId
import io.github.lvicentesanchez.cluster.sharding.Blueprint
import io.github.lvicentesanchez.data.{ Content, UserID }

import scala.concurrent.duration._

class User() extends Actor {
  import User.Protocol

  context.setReceiveTimeout(10.seconds)

  override val receive: Receive = {
    case msg: Protocol.SendMessage =>
      println(s"Actor ${context.self.path.name} received $msg")

    case ReceiveTimeout =>
      context.parent ! ShardRegion.Passivate(stopMessage = SupervisorStrategy.Stop)

    case SupervisorStrategy.Stop =>
      println(s"Actor ${context.self.path.name} is stopping!")
      context.stop(self)
  }
}

object User {

  object Protocol {
    final case class SendMessage(userID: UserID, content: Content) extends Command
  }

  object Blueprint extends Blueprint {
    import User.Protocol._

    override val extractID: ExtractEntityId = {
      case msg: SendMessage => (msg.userID.value, msg)
    }

    override val name: String = "User"

    override val props: Props = Props(new User())
  }
}
