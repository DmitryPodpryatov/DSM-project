import akka.http.scaladsl.server.Route
import endpoints.API

package object wiring {
  val route: Route = API.route
}
