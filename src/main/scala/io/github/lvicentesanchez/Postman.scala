package io.github.lvicentesanchez

import akka.actor.{ Props, ActorSystem }
import com.typesafe.config.ConfigFactory
import io.github.lvicentesanchez.actors.{ Bot, User }
import io.github.lvicentesanchez.actors.sharding.Sharding

object Postman extends App {
  val port = "2551"
  val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
    withFallback(ConfigFactory.load())

  val system = ActorSystem("PostmanSystem", config)
  val sharding = Sharding(system, 100)
  val actorRef = sharding.of(User.Blueprint)

  system.actorOf(Props[Bot], "bot")
}
