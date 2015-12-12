package io.github.lvicentesanchez.actors

import akka.actor.{ Actor, Props }
import akka.cluster.sharding.ShardRegion.ExtractEntityId
import io.github.lvicentesanchez.actors.sharding.Blueprint

class User() extends Actor {
  override val receive: Receive = {
    case _ => ()
  }
}

object User {

  case class SendMessage(id: String)

  object Blueprint extends Blueprint {
    override val extractID: ExtractEntityId = {
      case msg @ SendMessage(id) => (id, msg)
    }

    override val name: String = "User"

    override val props: Props = Props(new User())
  }
}
