name := "simple-scala-blockchain"

version := "0.1"

scalaVersion := "2.13.6"

// Deps
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.9"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test"

resolvers += "Artima Maven Repository" at "https://repo.artima.com/releases"
