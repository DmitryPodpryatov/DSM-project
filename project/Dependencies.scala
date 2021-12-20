import sbt.librarymanagement.ModuleID
import sbt.librarymanagement.syntax._

object Dependencies {

  object common {
    def dependencies: Seq[ModuleID] = Seq(
      cats.core,
      cats.effect,
      monix.core,
      akka.actor,
      akka.stream,
      akka.cluster,
      akka.http,
      akka.json,
      akka.cors,
      sttp.akka,
      logging.core,
      logging.logback
    )
  }

  object cats {
    val core = "org.typelevel" %% "cats-core" % "2.7.0"
    val effect = "org.typelevel" %% "cats-effect" % "2.5.4"
  }

  object monix {
    val core = "io.monix" %% "monix" % "3.4.0"
  }

  object akka {
    val actor = "com.typesafe.akka" %% "akka-actor-typed" % "2.6.8"
    val stream = "com.typesafe.akka" %% "akka-stream" % "2.6.8"
    val cluster = "com.typesafe.akka" %% "akka-cluster-typed" % "2.6.8"
    val http = "com.typesafe.akka" %% "akka-http" % "10.2.7"
    val json = "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.7"
    val cors = "ch.megard" %% "akka-http-cors" % "1.1.2"
  }

  object sttp {
    val akka = "com.softwaremill.sttp.client3" %% "akka-http-backend" % "3.3.18"
  }

  object logging {
    val core = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
    val logback = "ch.qos.logback" % "logback-classic" % "1.2.7"
  }
}
