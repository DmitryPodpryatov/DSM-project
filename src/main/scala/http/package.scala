import monix.eval.Task
import sttp.capabilities.akka.AkkaStreams
import sttp.capabilities.{Effect, WebSockets}
import sttp.client3.{Request, Response, SttpBackend}

import scala.concurrent.Future

package object http {
  type HttpClient = SttpBackend[Future, AkkaStreams with WebSockets]

  implicit final class HttpClientOps(private val backend: HttpClient) {
    def send[T, R >: AkkaStreams with WebSockets with Effect[Future]](request: Request[T, R]): Future[Response[T]] =
      request.send(backend)

    def sendToTask[T, R >: AkkaStreams with WebSockets with Effect[Future]](request: Request[T, R]): Task[Response[T]] =
      Task.deferFuture {
        request.send(backend)
      }
  }
}
