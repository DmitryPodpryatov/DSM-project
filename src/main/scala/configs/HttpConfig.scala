package configs

import sttp.model.Uri

final case class HttpConfig(
    hostGameUrl: Uri,
    registerPlayerUrl: Uri
)
