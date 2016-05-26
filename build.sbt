name := "Play"

version := "1.0"

lazy val `play` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq( jdbc , cache , ws   , specs2 % Test )

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-core_2.11" % "1.6.1",
  "org.apache.spark" % "spark-sql_2.11" % "1.6.1",
  "com.typesafe.akka" % "akka-actor_2.11" % "2.4.6",
  "com.typesafe.akka" % "akka-slf4j_2.11" % "2.4.6",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.7.3",
  "mysql" % "mysql-connector-java" % "6.0.2"
  //"com.typesafe.play" % "play-json_2.11" % "2.5.3"
  //  exclude("com.fasterxml.jackson.core", "jackson-databind")
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

//routesGenerator := InjectedRoutesGenerator