package game

import board.{Board, Cell}
import cats.Applicative
import cats.syntax.applicative._

import scala.util.Random

trait Game[F[_]] {
  def create: F[Board]
}

object Game {
  final class GameImpl[F[_]: Applicative](random: Random) extends Game[F] {
    override def create: F[Board] = {
      val index: Int = random.nextInt(boardList.length)

      boardList(index).pure[F]
    }

    lazy val boardList: Vector[Board] = {
      lazy val boardListStrings = Vector(
        """CPPB
          |WWPW
          |WPPW
          |WPWW
          |BPPC
          |""".stripMargin
      )

      stringToBoard(boardListStrings)
    }

    def stringToBoard(boardListStrings: Vector[String]): Vector[Board] =
      boardListStrings.map { board =>
        Board(
          board
            .split("\n")
            .map { line =>
              line.map { char =>
                Cell.string2Cell(char)
              }.toVector
            }
            .toVector
        )
      }
  }

  def make[F[_]: Applicative]: GameImpl[F] = {
    val random = new Random

    new GameImpl[F](random)
  }
}
