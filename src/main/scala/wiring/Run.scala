package wiring

import akka.http.scaladsl.Http
import cats.effect.Sync

import scala.concurrent.duration._

object Run {
  def start[F[_]: Sync](implicit core: Core): F[Unit] = {
    import core.akkaModule.actorSystem
    import core.akkaModule.actorSystem.executionContext

    val interface: String = "0.0.0.0"
    val port: Int = 8080

    core.logger.info(f"Running Akka server at $interface: $port")

    Sync[F].delay {
      Http()
        .newServerAt(interface, port)
        .bind(route)
        .map(_.addToCoordinatedShutdown(hardTerminationDeadline = 10.seconds))
    }
  }
}
