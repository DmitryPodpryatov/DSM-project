package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.board.Cell.{string2Cell, Down, Finish, Start, Up}
import spray.json.{deserializationError, DefaultJsonProtocol, JsString, JsValue, RootJsonFormat}

package object board extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val format: RootJsonFormat[Cell] = new RootJsonFormat[Cell] {
    override def write(obj: Cell): JsValue = obj match {
      case Start      => JsString("S")
      case Finish     => JsString("F")
      case Up         => JsString("U")
      case Down       => JsString("D")
      case Cell.Left  => JsString("L")
      case Cell.Right => JsString("R")
    }
    override def read(json: JsValue): Cell = json match {
      case JsString(value) if value.length == 1 =>
        string2Cell(value(0))
      case _ => deserializationError("Expected Cell value")
    }
  }

  implicit val boardFormat: RootJsonFormat[Board] = jsonFormat1(Board)
}
