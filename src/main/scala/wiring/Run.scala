package wiring

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.Http
import cats.effect.ExitCode
import monix.eval.Task

object Run {
  def start(implicit actorSystem: ActorSystem, core: Core, endpoints: Route): Task[ExitCode] =
    Task
      .deferAction { scheduler =>
        val interface: String = "0.0.0.0"
        val port: Int = 80

        core.logger.info(f"Starting Akka server at $interface:$port")
        val bindingTask: Task[Http.ServerBinding] = Task.fromFuture {
          Http(actorSystem).newServerAt(interface, port).bind(endpoints)
        }
        core.logger.info(f"Running Akka server at $interface:$port")

        bindingTask.bracket { _ =>
          Task.never[Unit]
        } { binding =>
          core.logger.info("Shutting Akka down")

          Task {
            binding.unbind().onComplete(_ => actorSystem.terminate())(scheduler)
          }
        }
      }
      .as(ExitCode.Success)
}
