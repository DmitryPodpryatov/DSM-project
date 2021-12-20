package models.game

import models.board.Board
import models.player.{PlayerId, Players}

final case class Game(
    gameId: GameId,
    board: Board,
    host: PlayerId,
    players: Players
)
