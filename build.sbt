name := "cascading.scala"

version := "1.0"

scalaVersion := "2.9.1"

scalacOptions += "-deprecation"

resolvers ++= Seq( "maven.org" at "http://repo2.maven.org/maven2",
                   "conjars.org" at "http://conjars.org/repo" )


libraryDependencies += "org.apache.hadoop" % "hadoop-core" % "0.20.2"

libraryDependencies += "cascading" % "cascading-core" % "1.2.4-wip-95"

libraryDependencies += "log4j" % "log4j" % "1.2.16"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "0.9.28"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"