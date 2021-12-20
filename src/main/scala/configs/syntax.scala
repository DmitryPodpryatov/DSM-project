package configs

import sttp.model.Uri

object syntax {
  object http {
    class InvalidUriException(uri: String) extends RuntimeException(f"$uri is an invalid URI")

    implicit final class UriOps(private val uri: String) {
      def toUri: Uri = Uri.parse(uri).getOrElse(throw new InvalidUriException(uri))
    }
  }
}
