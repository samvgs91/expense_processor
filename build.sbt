// scalaVersion := "2.13.8"
scalaVersion := "2.13.12"
name := "expense-processor"
organization := "org.dcc.peru"
version := "1.0"

lazy val root = (project in file("."))
  .settings(
    // other project settings
    assemblyJarName in assembly := "expense-processor-1.0.jar",
    mainClass in assembly := Some("com.App.BCPProcessor")
  )

// assemblyMergeStrategy in assembly := {
//   case PathList("META-INF", xs @ _*) => MergeStrategy.discard
//   case x => MergeStrategy.first
// }

assemblyMergeStrategy in assembly := {
  case x if x.endsWith("module-info.class") => MergeStrategy.concat
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}


// assemblyMergeStrategy in assembly := {
//   case PathList("META-INF", xs @ _*) =>
//     (xs map {_.toLowerCase}) match {
//       case "versions/9/module-info.class":: xs => MergeStrategy.first
//       case "MANIFEST.MF" :: xs => MergeStrategy.first
//     }
//   case _ => MergeStrategy.deduplicate
// }

// libraryDependencies += "org.apache.logging.log4j" %% "log4j-api-scala" % "12.0"
libraryDependencies += "org.apache.poi" % "poi" % "5.2.5" 
libraryDependencies += "org.apache.poi" % "poi-ooxml" % "5.2.0"
libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.21.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.10" % Test
libraryDependencies += "com.lihaoyi" %% "upickle" % "0.9.5"


