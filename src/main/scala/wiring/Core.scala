package wiring

import akka.actor.ActorSystem
import com.typesafe.scalalogging.Logger
import configs.HttpConfig
import http.HttpClient
import monix.execution.Scheduler
import sttp.client3.akkahttp.AkkaHttpBackend

import scala.util.Random

class Core(implicit
    val actorSystem: ActorSystem,
    val scheduler: Scheduler,
    val httpClient: HttpClient,
    val httpConfig: HttpConfig,
    val logger: Logger,
    val random: Random
)

object Core {
  def make[F[_]](implicit actorSystem: ActorSystem, scheduler: Scheduler, logger: Logger): Core = {
    import configs.syntax.http._

    implicit val random: Random = new Random

    val signService: String =
      if (System.getenv("APP_ENV") == "docker")
        "http://sign-service:8082/api"
      else
        "http://0.0.0.0:8082/api"

    implicit val backend: HttpClient = AkkaHttpBackend.usingActorSystem(actorSystem)
    implicit val httpConfig: HttpConfig = HttpConfig(
      hostGameUrl = f"$signService/host".toUri,
      registerPlayerUrl = f"$signService/login".toUri
    )

    new Core
  }
}
