enablePlugins(JavaServerAppPackaging)

name := "reactive-pet-supplies-service"

version := "0.1"

organization := "nl.sogeti.reactivepetsupplies"

scalaVersion := "2.11.7"

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
                  "Spray Repository"    at "http://repo.spray.io")

libraryDependencies ++= {
  val akkaVersion       = "2.4.1"
  val sprayVersion      = "1.3.3"
  Seq(
    "com.typesafe.akka" %% "akka-actor"      % akkaVersion,
    "io.spray"          %% "spray-can"       % sprayVersion,
    "io.spray"          %% "spray-routing"   % sprayVersion,
    "io.spray"          %% "spray-json"      % "1.3.1",
    "com.typesafe.akka" %% "akka-slf4j"      % akkaVersion,
    "ch.qos.logback"    %  "logback-classic" % "1.1.2",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
    "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23",
    "org.mindrot" % "jbcrypt" % "0.3m",
    // Test dependencies
    "com.typesafe.akka" %% "akka-testkit"    % akkaVersion  % "test",
    "org.scalatest"     % "scalatest_2.11"  % "2.2.4"       % "test",
    "io.spray"          %% "spray-testkit"   % sprayVersion % "test",
    "org.specs2"        %% "specs2"          % "2.3.13"     % "test"
  )
}

// Assembly settings
mainClass in Global := Some("nl.sogeti.reactivepetsupplies.Main")

jarName in assembly := "quiz-management-server.jar"
