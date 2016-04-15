lazy val root = (project in file(".")).
  settings(
    name := "Checkout",
    version := "1.0",
    scalaVersion := "2.11.8",
		libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"
  )