package storage

import models.game.{Game, GameId}

import scala.collection.mutable

object GameStorage {
  val games: mutable.Map[GameId, Game] = mutable.Map.empty
}
