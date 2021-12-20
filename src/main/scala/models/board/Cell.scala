package models.board

sealed abstract class Cell

object Cell {
  case object Start extends Cell
  case object Finish extends Cell
  case object Up extends Cell
  case object Down extends Cell
  case object Left extends Cell
  case object Right extends Cell

  def string2Cell(c: Char): Cell =
    c match {
      case 'S' => Start
      case 'F' => Finish
      case 'U' => Up
      case 'D' => Down
      case 'L' => Left
      case 'R' => Right
      case v   => throw new InvalidCellIdentifierException(v)
    }

  private class InvalidCellIdentifierException(value: Char)
      extends RuntimeException(f"Invalid identifier $value for Cell object")
}
