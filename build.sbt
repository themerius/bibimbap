import AssemblyKeys._

organization := "io.bibimbap"

name := "bibimbap-bibtex"

version := "0.1.1"

scalaVersion := "2.11.1"

scalacOptions += "-deprecation"

scalacOptions += "-feature"

scalacOptions += "-unchecked"

libraryDependencies ++= Seq(
    "org.scalatest" % "scalatest_2.11" % "2.2.0" % "test",
    "org.apache.commons" % "commons-lang3" % "3.3.2"
)

assemblySettings
