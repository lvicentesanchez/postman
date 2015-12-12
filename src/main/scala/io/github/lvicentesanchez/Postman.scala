package io.github.lvicentesanchez

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import io.github.lvicentesanchez.cluster.api.{ ApplicationAPIImpl, UserAPIImpl }
import io.github.lvicentesanchez.cluster.sharding.Sharding
import io.github.lvicentesanchez.cluster.{ Application, Bot, User }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object Postman extends App {
  val port = "2551"
  val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
    withFallback(ConfigFactory.load())

  val system = ActorSystem("PostmanSystem", config)
  val sharding = Sharding(system, 100)
  val userRef = sharding.of(User.Blueprint)
  val userAPI = UserAPIImpl(userRef, 10.seconds)
  val applicationRef = sharding.of(Application.Blueprint)
  val applicationAPI = ApplicationAPIImpl(applicationRef, 10.seconds)

  system.actorOf(Bot.props(userAPI), "bot")

  for {
    _ <- applicationAPI.createApplication("an-application")
    e <- applicationAPI.getApplicationEndpoint("an-application")
    _ = println(s"Application endpoint $e")
  } yield ()
}
