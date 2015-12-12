package io.github.lvicentesanchez.cluster.sharding

import akka.actor.{ ActorRef, ActorSystem }
import akka.cluster.sharding.{ ClusterSharding, ClusterShardingSettings, ShardRegion }

class Sharding(system: ActorSystem, nrOfShards: Int) {

  def of(blueprint: Blueprint): ActorRef = {
    val extractShardID: ShardRegion.ExtractShardId =
      blueprint.extractID andThen {
        case (id, _) => String.valueOf(id.## % nrOfShards)
      }
    ClusterSharding(system).start(
      blueprint.name,
      blueprint.props,
      ClusterShardingSettings(system),
      blueprint.extractID,
      extractShardID
    )
  }
}

object Sharding {

  def apply(system: ActorSystem, nrOfShards: Int): Sharding = new Sharding(system, nrOfShards)
}
