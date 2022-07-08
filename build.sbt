name := "scala-scratch"

version := "0.1"

scalaVersion := "2.12.6"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.12"

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.9",
  "org.mockito" % "mockito-core" % "2.8.9",
  "org.scalatest" %% "scalatest" % "3.2.12" % Test,
  "org.slf4j" % "slf4j-log4j12" % "1.7.16",
  "org.typelevel" %% "cats" % "0.9.0",
  "org.typelevel" %% "cats-effect" % "1.3.0" withSources() withJavadoc()
)

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps",
  "-language:higherKinds",
  "-Ypartial-unification")