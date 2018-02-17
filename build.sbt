lazy val root = (project in file("."))
  .settings(Project.settings: _*)
  .settings(libraryDependencies += Dependencies.typesafe)
  .settings(libraryDependencies ++= Dependencies.slf4j: _*)
