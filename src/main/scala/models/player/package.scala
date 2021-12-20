package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

import scala.collection.mutable

package object player extends DefaultJsonProtocol with SprayJsonSupport {
  type PlayerId = String
  type Players = mutable.Set[Player]

  type Position = (Int, Int)

  implicit val playerFormat: RootJsonFormat[Player] = jsonFormat2(Player)
  implicit val playersFormat: RootJsonFormat[Players] = mutableSetFormat[Player]

  // Mutable JSON formats

  implicit def mutableSetFormat[T: RootJsonFormat]: RootJsonFormat[mutable.Set[T]] =
    viaSeqMutable[mutable.Set[T], T](seq => seq.to(mutable.Set))

  def viaSeqMutable[I <: Iterable[T], T: RootJsonFormat](f: mutable.Seq[T] => I): RootJsonFormat[I] =
    new RootJsonFormat[I] {
      def write(iterable: I): JsArray = JsArray(iterable.iterator.map(_.toJson).toVector)
      def read(value: JsValue): I = value match {
        case JsArray(elements) => f(elements.map(_.convertTo[T]).to(mutable.Seq))
        case x                 => deserializationError("Expected Collection as JsArray, but got " + x)
      }
    }
}
