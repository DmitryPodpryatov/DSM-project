import sbt.addCompilerPlugin

name := "DSM-project"
version := "0.1.0-SNAPSHOT"
scalaVersion := "2.13.7"

lazy val commonSettings = Seq(
  libraryDependencies ++= Dependencies.common.dependencies,
  scalacOptions ++= Seq(
    "-language:higherKinds",
    "-unchecked",
    "-deprecation",
  ),
  resolvers ++= Resolvers.resolvers,
  addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full),
)

lazy val `DSM-project-root` = (project in file("."))
  .enablePlugins(DockerPlugin)
  .settings(
    commonSettings,
    Docker.peer
  )

lazy val `sign-service` = (project in file("sign-service"))
  .enablePlugins(DockerPlugin)
  .settings(
    commonSettings,
    Docker.`sign-service`
  )
