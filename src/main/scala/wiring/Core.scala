package wiring

import cats.Applicative
import cats.syntax.functor._
import com.typesafe.scalalogging.Logger

import scala.util.Random

class Core(implicit
    val akkaModule: Akka,
    val logger: Logger,
    val random: Random
)

object Core {
  def make[F[_]: Applicative](implicit logger: Logger): F[Core] = {
    implicit val random: Random = new Random

    for {
      implicit0(akkaModule: Akka) <- Akka.make[F]
    } yield new Core
  }
}
