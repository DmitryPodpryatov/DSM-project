package wiring

import akka.actor.typed.{ActorSystem, Scheduler}
import akka.actor.typed.scaladsl.Behaviors
import cats.Applicative
import cats.syntax.applicative._

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

class Akka(implicit
    val actorSystem: ActorSystem[Nothing],
    val scheduler: Scheduler,
    val executionContext: ExecutionContext
)

object Akka {
  def make[F[_]: Applicative]: F[Akka] = {
    implicit val actorSystem: ActorSystem[Nothing] = ActorSystem[Nothing](Behaviors.empty, "dsm-project")
    implicit val scheduler: Scheduler = actorSystem.scheduler
    implicit val executionContext: ExecutionContextExecutor = actorSystem.executionContext
    (new Akka).pure[F]
  }
}
