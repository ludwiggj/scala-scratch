name := "scala-scratch"

version := "0.1"

scalaVersion := "2.13.10"

resolvers ++= Resolver.sonatypeOssRepos("releases") ++ Resolver.sonatypeOssRepos("snapshots")

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.10",
  "org.mockito" % "mockito-core" % "4.11.0",
  "org.scalactic" %% "scalactic" % "3.2.15",
  "org.scalatest" %% "scalatest" % "3.2.15" % Test,
  "org.slf4j" % "slf4j-log4j12" % "2.0.5",
  "org.typelevel" %% "cats-core" % "2.9.0",
  "org.typelevel" %% "cats-effect" % "3.4.5" withSources() withJavadoc(),
  "org.typelevel" %% "mouse" % "1.2.1",
  "co.fs2" %% "fs2-core" % "3.5.0",
)

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps",
  "-language:higherKinds")