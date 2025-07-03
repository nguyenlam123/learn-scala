val scala2 = "2.13.12"
val scala3 = "3.6.4"

val toolkitTest = "org.scala-lang" %% "toolkit-test" % "0.1.7"

val catsEffect = "org.typelevel" %% "cats-effect" % "3.6.1" withSources() withJavadoc()

ThisBuild / scalaVersion := scala3
ThisBuild / organization := "com.example"

lazy val hello = project
  .in(file("."))
  .aggregate(helloCore)
  .dependsOn(helloCore)
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "Hello",
    libraryDependencies += toolkitTest % Test
  )

lazy val helloCore = project
  .in(file("core"))
  .settings(
    name := "Hello Core",
    libraryDependencies += "org.scala-lang" %% "toolkit" % "0.1.7",
    libraryDependencies += toolkitTest % Test,
    libraryDependencies += catsEffect
  )
