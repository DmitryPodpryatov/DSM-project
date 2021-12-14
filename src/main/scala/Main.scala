import cats.effect.ExitCode
import com.typesafe.scalalogging.{LazyLogging, Logger}
import monix.eval.{Task, TaskApp}

object Main extends TaskApp with LazyLogging {
  implicit val logger_ : Logger = logger

  override def run(args: List[String]): Task[ExitCode] =
    Task(Application.run[Task]).as(ExitCode.Success)
}
