import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import spray.json.DefaultJsonProtocol

import scala.collection.mutable
import scala.util.Random

object Endpoints extends DefaultJsonProtocol with SprayJsonSupport {
  val route: Route = cors() {
    concat(
      path("api" / "login") {
        entity(as[String]) { host =>
          post {
            complete(login(host))
          }
        }
      },
      path("api" / "host") {
        parameter("playerId") { playerId =>
          post {
            complete(hostGame(playerId))
          }
        }
      },
      path("api" / "games") {
        get {
          complete(listGame)
        }
      },
      path("api" / "players") {
        get {
          complete(listPlayers)
        }
      }
    )
  }

  private val games: mutable.Map[String, String] = mutable.Map.empty[String, String]
  private val players: mutable.Map[String, String] = mutable.Map.empty[String, String]

  val random: Random.type = Random

  def login(host: String): String = {
    val playerId: String = random.nextInt(100000).toString
    players.put(playerId, host)

    playerId
  }

  def hostGame(playerId: String): HttpResponse =
    if (players.contains(playerId)) {
      val gameId: String = random.nextInt(100000).toString
      games.put(gameId, playerId)

      HttpResponse(entity = gameId)
    } else {
      HttpResponse(StatusCodes.InternalServerError, entity = "Player is not registered")
    }

  def listGame: Map[String, String] = games.toMap

  def listPlayers: Map[String, String] = players.toMap
}
