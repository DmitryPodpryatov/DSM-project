package endpoints

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import cats.effect.Effect
import models.game.Game
import monix.eval.Task
import monix.execution.Scheduler
import services.GameService

object GameEndpoints {
  def routes[F[_]: Effect](gameService: GameService[F])(implicit scheduler: Scheduler): Route = {
    val createGameRoute: Route = path("api" / "game") {
      parameter("playerId") { playerId =>
        post {
          complete {
            Task.from(gameService.hostGame(playerId)).runToFuture
          }
        }
      }
    }

    val joinGame: Route = path("api" / "game" / Segment / "join") { gameId =>
      parameter("playerId") { playerId =>
        post {
          complete {
            Task.from(gameService.joinGame(gameId, playerId)).runToFuture
          }
        }
      }
    }

    val setupGame: Route = path("api" / "game" / "setup") {
      post {
        entity(as[Game]) { game =>
          complete {
            Task.from(gameService.setupGame(game)).runToFuture
          }
        }
      }
    }

    val startGame: Route = path("api" / "game" / Segment / "start") { gameId =>
      parameter("playerId") { playerId =>
        post {
          complete {
            Task.from(gameService.startGame(gameId, playerId)).runToFuture
          }
        }
      }
    }

    val getGame: Route = path("api" / "game" / Segment) { gameId =>
      get {
        complete {
          Task.from(gameService.getGame(gameId)).runToFuture
        }
      }
    }

    val move: Route = path("api" / "game" / Segment / "move") { gameId =>
      parameter("playerId") { playerId =>
        post {
          complete {
            Task.from(gameService.move(gameId, playerId)).runToFuture
          }
        }
      }
    }

    val status: Route = path("api" / "game" / Segment / "winner") { gameId =>
      parameter("playerId") { playerId =>
        get {
          complete {
            Task.from(gameService.status(gameId, playerId)).runToFuture
          }
        }
      }
    }

    val routes: Route = concat(
      createGameRoute,
      joinGame,
      setupGame,
      startGame,
      getGame,
      move,
      status
    )

    routes
  }
}
