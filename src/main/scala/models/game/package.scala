package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

package object game extends DefaultJsonProtocol with SprayJsonSupport {
  type GameId = String

  implicit val gameFormat: RootJsonFormat[Game] = jsonFormat4(Game)
}
