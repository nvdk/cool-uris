val ScalatraVersion = "3.1.0"

organization := "eu.vandekeybus"

name := "cool-uris"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.13.15"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra-jakarta" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest-jakarta" % ScalatraVersion % "test",
  "ch.qos.logback" % "logback-classic" % "1.5.11" % "runtime",
  "org.slf4j" % "jcl-over-slf4j" % "2.0.16",
  "org.eclipse.jetty.ee10" % "jetty-ee10-webapp" % "12.0.14" % "container;compile",
  "jakarta.servlet" % "jakarta.servlet-api" % "6.0.0" % "provided",
  "org.eclipse.rdf4j" % "rdf4j-repository-sparql" % "5.1.0",
  "org.eclipse.rdf4j" % "rdf4j-queryresultio-sparqljson" % "5.1.0"
)

enablePlugins(ScalatraPlugin)

ThisBuild / assemblyMergeStrategy := {
  case "module-info.class" => MergeStrategy.discard
  case "META-INF/versions/9/module-info.class" => MergeStrategy.discard
  case x =>
    val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
    oldStrategy(x)
}
