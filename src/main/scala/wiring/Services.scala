package wiring

import monix.eval.Task
import services.{BoardService, GameService}

class Services(implicit
    val gameService: GameService[Task]
)

object Services {
  def make(core: Core): Services = {
    implicit val boardService: BoardService[Task] = BoardService.make[Task](core.random)
    implicit val gameService: GameService[Task] = GameService.make(boardService, core.random, core.httpClient, core.httpConfig)

    new Services
  }
}
