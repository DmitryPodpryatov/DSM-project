import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.DebuggingDirectives
import cats.effect.ExitCode
import com.typesafe.scalalogging.{LazyLogging, Logger}
import monix.eval.{Task, TaskApp}

object SignServiceMain extends TaskApp with LazyLogging {
  val logger_ : Logger = logger

  override def run(args: List[String]): Task[ExitCode] =
    Task
      .deferAction { scheduler =>
        implicit val actorSystem: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")

        val interface = "0.0.0.0"
        val port = 8082

        val endpoints: Route = DebuggingDirectives.logRequestResult("Sign Service", Logging.InfoLevel)(Endpoints.route)

        logger_.info(f"Starting Akka server at $interface:$port")
        val binding: Task[Http.ServerBinding] = Task.deferFuture {
          Http().newServerAt(interface, port).bind(endpoints)
        }
        logger_.info(f"Running Akka server at $interface:$port")

        binding.bracket { _ =>
          Task.never[Unit]
        } { binding =>
          logger_.info("Shutting Akka down")

          Task {
            binding.unbind().onComplete(_ => actorSystem.terminate())(scheduler)
          }
        }
      }
      .as(ExitCode.Success)
}
