package board

sealed abstract class Cell

object Cell {
  case object Path extends Cell
  case object Wall extends Cell
  case object Castle extends Cell
  case object Barrack extends Cell

  def string2Cell(c: Char): Cell =
    c match {
      case 'P' => Path
      case 'W' => Wall
      case 'C' => Castle
      case 'B' => Barrack
      case v   => throw new InvalidCellIdentifierException(v)
    }

  private class InvalidCellIdentifierException(value: Char)
      extends RuntimeException(f"Invalid identifier $value for Cell object")
}
