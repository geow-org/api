
scalaVersion := "2.11.2"

scalacOptions ++= Seq("-Xmax-classfile-name", "254")

lazy val root = project.in( file(".") )
	.settings(
		organization := "org.geow",
		scalaVersion := "2.11.2"
	)
	.aggregate(geohash, model, parser, testkit, tests, util)

lazy val geohash = project
	.settings(
		organization := "org.geow",
		name := """geohash""",
		scalaVersion := "2.11.2"
	)

lazy val model = project
	.settings(
		organization := "org.geow",
		name := """model""",
		scalaVersion := "2.11.2",
		scalacOptions ++= Seq("-deprecation"),
		parallelExecution := false,
		resolvers += Resolver.sonatypeRepo("snapshots"),
    	libraryDependencies ++= Seq(
			"org.scala-lang" %% "scala-pickling" % "0.9.0-SNAPSHOT",
  			"org.scalacheck" %% "scalacheck" % "1.11.4" % "test",
    		"org.specs2" %% "specs2" % "2.3.12" % "test",
    		"junit" % "junit" % "4.11" % "test"
		)
	)

lazy val parser = project.dependsOn(model, geohash)
	.settings(
		organization := "org.geow",
		name := """parser""",
		scalaVersion := "2.11.2",
		resolvers += Resolver.sonatypeRepo("snapshots"),
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






