

lazy val root = project.in( file(".") )
	.aggregate(geohash, model, parser)

lazy val geohash = project
	.settings(
		scalaVersion := "2.11.2",
		scalacOptions ++= Seq("-Xmax-classfile-name", "254","-deprecation"),
		resolvers ++= Seq(
			Resolver.sonatypeRepo("snapshots"),
			Resolver.sonatypeRepo("releases")
		),
		organization := "org.geow",
		name := """geohash""",
		version := "0.1-SNAPSHOT",
		parallelExecution := false
	)

lazy val model = project
	.settings(
		scalaVersion := "2.11.2",
		scalacOptions ++= Seq("-Xmax-classfile-name", "254","-deprecation"),
		resolvers ++= Seq(
			Resolver.sonatypeRepo("snapshots"),
			Resolver.sonatypeRepo("releases")
		),
		organization := "org.geow",
		name := """model""",
		version := "0.1-SNAPSHOT",
		parallelExecution := false,
    	libraryDependencies ++= Seq(
			"org.scala-lang" %% "scala-pickling" % "0.9.0-SNAPSHOT",
  			"org.scalacheck" %% "scalacheck" % "1.11.4",
    		"org.specs2" %% "specs2" % "2.3.12" % "test",
    		"junit" % "junit" % "4.11" % "test"
		)
	)

lazy val parser = project.dependsOn(model, geohash)
	.settings(
		scalaVersion := "2.11.2",
		scalacOptions ++= Seq("-Xmax-classfile-name", "254","-deprecation"),
		resolvers ++= Seq(
			Resolver.sonatypeRepo("snapshots"),
			Resolver.sonatypeRepo("releases")
		),
		organization := "org.geow",
		name := """parser""",
		version := "0.1-SNAPSHOT",
    	libraryDependencies ++= Seq(
			 "org.scala-lang" % "scala-xml" % "2.11.0-M4",
			"joda-time" % "joda-time" % "2.3",
  			"org.scalacheck" %% "scalacheck" % "1.11.4" % "test",
    		"org.specs2" %% "specs2" % "2.3.12" % "test",
    		"junit" % "junit" % "4.11" % "test"
		)
	)

	





