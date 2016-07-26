name := "SPAN_Assessment"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-stream_2.11" % "2.4.8",
  "org.scalaz" %% "scalaz-core" % "7.2.0",
  "org.scalatest" % "scalatest_2.11" % "2.2.6" % "test"
)