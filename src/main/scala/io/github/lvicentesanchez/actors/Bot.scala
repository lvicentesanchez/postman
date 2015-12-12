package io.github.lvicentesanchez.actors

import java.util.UUID
import scala.concurrent.duration._
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.cluster.sharding.ClusterSharding

object Bot {
  private case object Tick
}

class Bot extends Actor with ActorLogging {
  import Bot._
  import context.dispatcher
  val tickTask = context.system.scheduler.schedule(3.seconds, 3.seconds, self, Tick)

  val userRef = ClusterSharding(context.system).shardRegion(User.Blueprint.name)

  override def postStop(): Unit = {
    super.postStop()
    tickTask.cancel()
  }

  def receive = create

  val create: Receive = {
    case Tick =>
      val userID = UUID.randomUUID().toString
      userRef ! User.SendMessage(userID, String.valueOf(userID.##))
  }
}
