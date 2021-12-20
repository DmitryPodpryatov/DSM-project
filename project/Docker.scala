import sbt._
import sbt.Keys._
import sbtdocker.DockerKeys.{docker, dockerfile, imageNames}
import sbtdocker.{Dockerfile, ImageName}

object Docker {
  // noinspection TypeAnnotation
  def peer =
    generateDocker("dsm-project/peer:stable", 80, static = true)

  // noinspection TypeAnnotation
  def `sign-service` =
    generateDocker("dsm-project/sign-service:stable", 8082)

  private def generateDocker(imageName: String, port: Int, static: Boolean = false) = Seq(
    docker / dockerfile := {
      val jarFile = (Compile / packageBin / sbt.Keys.`package`).value
      val jarTarget = s"/app/${jarFile.getName}"

      val classpath = (Compile / managedClasspath).value
      val classpathString = classpath.files.map("/app/" + _.getName).mkString(":") + ":" + jarTarget

      val `main-class` =
        (Compile / packageBin / mainClass).value.getOrElse(sys.error("Expected exactly one main class"))

      if (static) {
        val staticFiles = {
          val resources = (Compile / baseDirectory).value.listFiles()

          resources.find(_.getPath.endsWith("/static")).get
        }

        new Dockerfile {
          from("openjdk:8-jre")
          copy(classpath.files, "/app/")
          copy(jarFile, jarTarget)
          add(staticFiles, "/app/static/")
          expose(port)
          entryPoint("java", "-cp", classpathString, `main-class`)
        }
      } else {
        new Dockerfile {
          from("openjdk:8-jre")
          copy(classpath.files, "/app/")
          copy(jarFile, jarTarget)
          expose(port)
          entryPoint("java", "-cp", classpathString, `main-class`)
        }
      }
    },
    docker / imageNames := Seq(
      ImageName(imageName)
    )
  )
}
