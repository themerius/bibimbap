seq(assemblySettings : _*)

organization := "io.bibimbap"

name := "bibimbap"

version := "0.1.0"

scalaVersion := "2.10.0"

scalacOptions += "-deprecation"

scalacOptions += "-feature"

scalacOptions += "-unchecked"

//fork := true

//javaOptions in (Test, run) += "-Djline.shutdownhook=false"

libraryDependencies ++= Seq(
    "org.scalatest" % "scalatest_2.10.0" % "1.8",
    "jline" % "jline" % "0.9.94",
    "org.apache.lucene" % "lucene-core" % "3.6.0",
    "commons-io" % "commons-io" % "2.4",
    "org.apache.commons" % "commons-lang3" % "3.1",
    "com.typesafe.akka" %% "akka-actor" % "2.1.0"
)

mainClass in (Compile, run) := Some("io.bibimbap.Main")
