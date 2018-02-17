import sbt._

object Dependencies {

  val typesafe = "com.typesafe" % "config" % Version.typesafe

  val slf4j = Seq(
    "org.slf4j" % "slf4j-api" % Version.slf4j,
    "org.slf4j" % "slf4j-simple" % Version.slf4j
  )

  object Version {
    val typesafe = "1.3.1"
    val slf4j = "1.7.5"
  }

}
