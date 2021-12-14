import cats.Monad
import cats.effect.Sync
import cats.syntax.functor._
import com.typesafe.scalalogging.Logger
import wiring.{Core, Run}

object Application {
  def run[F[_]: Sync: Monad](implicit logger: Logger): F[Unit] =
    for {
      implicit0(core: Core) <- Core.make[F]
    } yield Run.start[F]
}
