import akka.actor.ActorSystem
import akka.cluster.Cluster
import cats.effect.ExitCode
import com.typesafe.scalalogging.{LazyLogging, Logger}
import monix.eval.{Task, TaskApp}
import monix.execution.Scheduler

object Main extends TaskApp with LazyLogging {
  implicit val actorSystem: ActorSystem = ActorSystem("dsm-project")
//  implicit val actorCluster: Cluster = Cluster(actorSystem)
  implicit val scheduler_ : Scheduler = scheduler
  implicit val logger_ : Logger = logger

  override def run(args: List[String]): Task[ExitCode] = Application.run
}
