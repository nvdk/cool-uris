val ScalatraVersion = "2.5.3"

organization := "eu.vandekeybus"

name := "cool-uris"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.3"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.1.5" % "runtime",
  "org.slf4j" %"jcl-over-slf4j" % "1.7.16",
  "org.eclipse.jetty" % "jetty-webapp" % "9.2.15.v20160210" % "container;compile",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "org.eclipse.rdf4j" % "rdf4j-repository-sparql" % "2.2.2"
)

enablePlugins(ScalatraPlugin)
