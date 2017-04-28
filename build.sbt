name := "nfl-live-stream"
version := "1.0"
scalaVersion := "2.12.1"

val http4sVersion = "0.15.9"
val circeVersion = "0.6.1"
val monixVersion = "2.2.4"

// http4s dependencies
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion
)

// circe dependencies
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

// Monix
libraryDependencies += "io.monix" %% "monix" % monixVersion
