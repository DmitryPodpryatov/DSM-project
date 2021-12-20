package endpoints

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object DefaultEndpoints {
  private val prefix: String =
    if (System.getenv("APP_ENV") == "docker")
      "/app/static"
    else
      "/static"

  val `js-css-images`: Route = concat(
    path("js" / Segment) { filename =>
      getFromDirectory(f"$prefix/js/$filename")
    },
    path("css" / Segment) { filename =>
      getFromDirectory(f"$prefix/css/$filename")
    },
    path("images" / Segment) { filename =>
      getFromDirectory(f"$prefix/images/$filename")
    }
  )

  def routes: Route = {
    val staticRoute: Route = concat(
      path("") {
        getFromFile(f"$prefix/index.html")
      },
      path("games") {
        getFromFile(f"$prefix/games.html")
      },
      path("games" / Segment) { gameId =>
        parameter("playerId") { playerId =>
          getFromFile(f"$prefix/game.html")
        }
      },
      `js-css-images`,
      pathPrefix("games") {
        `js-css-images`
      }
    )

    val testRoute: Route = path("api" / "test") {
      get {
        complete("test")
      }
    }

    val routes: Route = concat(
      staticRoute,
      testRoute
    )

    routes
  }
}
