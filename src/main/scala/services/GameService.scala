package services

import configs.HttpConfig
import http._
import models.board.{Board, Cell}
import models.game.{Game, GameId}
import models.player.{Player, PlayerId, Players, Position}
import monix.eval.Task
import storage.GameStorage
import sttp.client3._

import scala.collection.mutable
import scala.util.Random

trait GameService[F[_]] {
  def hostGame(playerId: PlayerId): F[GameId]
  def joinGame(gameId: GameId, playerId: PlayerId): F[GameId]
  def setupGame(game: Game): F[Game]
  def startGame(gameId: GameId, host: PlayerId): F[Game]
  def getGame(gameId: GameId): F[Game]
  def move(gameId: GameId, playerId: PlayerId): F[Game]
  def status(gameId: GameId, playerId: PlayerId): F[Option[PlayerId]]
}

object GameService {
  final class Impl(
      boardService: BoardService[Task],
      random: Random,
      httpClient: HttpClient,
      httpConfig: HttpConfig
  ) extends GameService[Task] {
    override def hostGame(playerId: PlayerId): Task[GameId] = for {
      board <- boardService.createBoard
      gameId <- registerGame(playerId)
      game = Game(
        gameId,
        board,
        playerId,
        mutable.Set(defaultPlayer(playerId, board))
      )

      _ = GameStorage.games.put(gameId, game)
    } yield gameId

    private def registerGame(playerId: PlayerId): Task[GameId] = {
      val request: Request[String, Any] =
        basicRequest
          .post(uri"${httpConfig.hostGameUrl}?playerId=$playerId")
          .response(asStringAlways)

      for {
        response <- httpClient.sendToTask(request)
      } yield response.body
    }

    def registerPlayer: Task[PlayerId] = {
      val request: Request[String, Any] =
        basicRequest
          .post(httpConfig.registerPlayerUrl)
          .response(asStringAlways)

      for {
        response <- httpClient.sendToTask(request)
      } yield response.body
    }

    override def joinGame(gameId: GameId, playerId: PlayerId): Task[GameId] =
      if (GameStorage.games.contains(gameId) && gameById(gameId).host != playerId) {
        val board: Board = gameById(gameId).board

        gameById(gameId).players += defaultPlayer(playerId, board)

        Task(gameId)
      } else {
        throw new InvalidGameConfiguration
      }

    private def defaultPlayer(playerId: PlayerId, board: Board): Player = {
      val startPosition: Position = cellPosition(Cell.Start, board)

      Player(
        playerId,
        startPosition
      )
    }

    private def cellPosition(cell: Cell, board: Board): Position = {
      val flat: Vector[Cell] = board.cells.flatten

      val height: Int = board.cells.length
      val width: Int = flat.length / height

      val flatIndex: Int = flat.indexOf(cell)

      val firstIndex: Int = flatIndex * width / flat.length
      val secondIndex = flatIndex % width

      (firstIndex, secondIndex)
    }

    def setupGame(game: Game): Task[Game] =
      if (!GameStorage.games.contains(game.gameId)) {
        GameStorage.games += game.gameId -> game

        Task(game)
      } else {
        throw new InvalidGameConfiguration
      }

    override def startGame(gameId: GameId, host: PlayerId): Task[Game] = {
      validateHost(gameId, host)

      Task(gameById(gameId))
    }

    def getGame(gameId: GameId): Task[Game] =
      if (GameStorage.games.contains(gameId)) {
        Task(gameById(gameId))
      } else {
        throw new InvalidGameConfiguration
      }

    override def move(gameId: GameId, playerId: PlayerId): Task[Game] = {
      validatePlayer(gameId, playerId)

      val board: Board = gameById(gameId).board
      val moveLength: Int = random.between(1, 7)
      val player: Player = calculateMove(gameId, playerId, board, moveLength)

      val players: Players = gameById(gameId).players
      players.filterInPlace(_.playerId != playerId)
      players += player

      Task(gameById(gameId))
    }

    private def calculateMove(gameId: GameId, playerId: PlayerId, board: Board, moveLength: Int): Player = {
      val setOfOnePlayer: mutable.Set[Player] = gameById(gameId).players.filter(_.playerId == playerId)
      assert(setOfOnePlayer.toSeq.length == 1)

      val player: Player = setOfOnePlayer.head
      val initialPosition: Position = player.position

      val finalPosition: Position = {
        val flat: Vector[Cell] = board.cells.flatten
        val height = board.cells.length
        val width = flat.length / height

        val flatFinalIndex: Int = (initialPosition._1 * width + initialPosition._2) + moveLength

        if (flatFinalIndex >= flat.length)
          (height, width)
        else {
          val firstIndex: Int = flatFinalIndex * width / flat.length
          val secondIndex: Int = flatFinalIndex % width

          (firstIndex, secondIndex)
        }
      }

      player.copy(position = finalPosition)
    }

    override def status(gameId: GameId, playerId: PlayerId): Task[Option[PlayerId]] = {
      validatePlayer(gameId, playerId)

      val board: Board = gameById(gameId).board

      val finish: Position = cellPosition(Cell.Finish, board)

      Task {
        gameById(gameId).players.find(_.position == finish).map(_.playerId)
      }
    }

    private def gameById(gameId: GameId): Game =
      GameStorage.games(gameId)

    def validatePlayer(gameId: GameId, playerId: PlayerId): Unit =
      if (!GameStorage.games.contains(gameId) || gameById(gameId).players.forall(_.playerId != playerId))
        throw new InvalidGameConfiguration

    def validateHost(gameId: GameId, host: PlayerId): Unit =
      if (!GameStorage.games.contains(gameId) || gameById(gameId).host != host)
        throw new InvalidGameConfiguration

    private class InvalidGameConfiguration extends RuntimeException(s"Invalid game configuration")
  }

  def make(
      boardService: BoardService[Task],
      random: Random,
      httpClient: HttpClient,
      httpConfig: HttpConfig
  ): GameService[Task] =
    new Impl(boardService, random, httpClient, httpConfig)
}
