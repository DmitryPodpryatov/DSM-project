import akka.actor.ActorSystem
import akka.cluster.Cluster
import akka.http.scaladsl.server.Route
import cats.effect.ExitCode
import com.typesafe.scalalogging.Logger
import monix.eval.Task
import monix.execution.Scheduler
import wiring.{Core, Endpoints, Run, Services}

object Application {
  def run(implicit
      actorSystem: ActorSystem,
//      actorCluster: Cluster,
      scheduler: Scheduler,
      logger: Logger
  ): Task[ExitCode] = {
    implicit val core: Core = Core.make
    implicit val services: Services = Services.make(core)
    implicit val endpoints: Route = Endpoints.make(services)

    Run.start
  }
}
