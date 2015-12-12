package io.github.lvicentesanchez

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import io.github.lvicentesanchez.cluster.api.UserAPIImpl
import io.github.lvicentesanchez.cluster.sharding.Sharding
import io.github.lvicentesanchez.cluster.{ Bot, User }

import scala.concurrent.duration._

object Postman extends App {
  val port = "2551"
  val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
    withFallback(ConfigFactory.load())

  val system = ActorSystem("PostmanSystem", config)
  val sharding = Sharding(system, 100)
  val userRef = sharding.of(User.Blueprint)
  val userAPI = UserAPIImpl(userRef, 10.seconds)

  system.actorOf(Bot.props(userAPI), "bot")
}
