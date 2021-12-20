package wiring

import akka.event.Logging
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.DebuggingDirectives
import ch.megard.akka.http.cors.scaladsl.CorsDirectives.cors
import endpoints.{DefaultEndpoints, GameEndpoints}
import monix.execution.Scheduler

object Endpoints {
  def make(services: Services)(implicit scheduler: Scheduler): Route = {
    val routes: Route = cors() {
      concat(
        DefaultEndpoints.routes,
        GameEndpoints.routes(services.gameService)
      )
    }

    DebuggingDirectives.logRequestResult("Peer", Logging.InfoLevel)(routes)
  }
}
