name := "akka-http-line-bot"
organization := "xoyo24.example"
version := "1.0.0"
scalaVersion := "2.12.0"

enablePlugins(JavaAppPackaging)

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV = "2.4.14"
  val akkaHttpV = "10.0.0"

  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV,
    "org.scalatest" %% "scalatest" % "3.0.1" % Test,
    "org.scalamock" %% "scalamock-scalatest-support" % "3.4.2" % Test
  )
}

Revolver.settings
