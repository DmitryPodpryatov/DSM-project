import sbt.librarymanagement.{MavenRepository, Resolver}
import sbt.librarymanagement.syntax._

object Resolvers {
  def resolvers: Seq[MavenRepository] = Seq(
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    Resolver.mavenLocal
  )
}
