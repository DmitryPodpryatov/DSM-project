package services

import cats.Applicative
import cats.syntax.applicative._
import models.board.{Board, Cell}

import scala.util.Random

trait BoardService[F[_]] {
  def createBoard: F[Board]
}

object BoardService {
  final class Impl[F[_]: Applicative](random: Random) extends BoardService[F] {
    override def createBoard: F[Board] = {
      val index: Int = random.nextInt(boardList.length)

      boardList(index)
    }.pure[F]

    lazy val boardList: Vector[Board] = {
      lazy val boardListStrings = Vector(
        """SRRRD
          |DLLLL
          |RRRRD
          |DLLLL
          |RRRRF
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

  def make[F[_]: Applicative](random: Random): Impl[F] =
    new Impl[F](random)
}
