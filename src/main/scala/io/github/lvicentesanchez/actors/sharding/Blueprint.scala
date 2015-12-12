package io.github.lvicentesanchez.actors.sharding

import akka.actor.Props
import akka.cluster.sharding.ShardRegion

trait Blueprint {
  def extractID: ShardRegion.ExtractEntityId
  def name: String
  def props: Props
}
