package io.github.lvicentesanchez.cluster

import java.util.UUID

import akka.actor.{ Actor, ActorLogging, Props }
import io.github.lvicentesanchez.cluster.api.UserAPI
import io.github.lvicentesanchez.data.{ Content, UserID }

import scala.concurrent.duration._

class Bot(userAPI: UserAPI) extends Actor with ActorLogging {
  import Bot._
  import context.dispatcher
  val tickTask = context.system.scheduler.schedule(3.seconds, 3.seconds, self, Tick)

  override def postStop(): Unit = {
    super.postStop()
    tickTask.cancel()
  }

  override val receive: Receive = {
    case Tick =>
      val userID = UUID.randomUUID().toString
      userAPI.sendMessage(UserID(userID), Content(String.valueOf(userID.##)))
  }
}

object Bot {
  def props(userAPI: UserAPI): Props = Props(new Bot(userAPI))

  private case object Tick
}
