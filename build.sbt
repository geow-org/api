name := """geow-api"""

version := "1.0"


lazy val root = project.in( file(".") )
	.settings(
		scalaVersion := "2.11.1"
	)
	.aggregate(geohash, model, parser, testkit, tests, util)

lazy val geohash = project
	.settings(
		scalaVersion := "2.11.1"
	)

lazy val model = project.dependsOn(geohash)
	.settings(
		scalaVersion := "2.11.1",
    	libraryDependencies ++= Seq(
			"org.scala-lang" %% "scala-pickling" % "0.8.0",
  			"org.scalacheck" %% "scalacheck" % "1.11.4" % "test",
    		"org.specs2" %% "specs2" % "2.3.12" % "test",
    		"junit" % "junit" % "4.11" % "test"
		)
	)

lazy val parser = project.dependsOn(model)
	.settings(
		scalaVersion := "2.11.1",
    	libraryDependencies ++= Seq(
			 "org.scala-lang" % "scala-xml" % "2.11.0-M4",
			 "joda-time" % "joda-time" % "2.3",
  			"org.scalacheck" %% "scalacheck" % "1.11.4" % "test",
    		"org.specs2" %% "specs2" % "2.3.12" % "test",
    		"junit" % "junit" % "4.11" % "test"
		)
	)

lazy val testkit = project

lazy val tests = project

lazy val util = project
	

resolvers ++= Seq(
	Resolver.sonatypeRepo("snapshots")
)






