// put this at the top of the file
import scalariform.formatter.preferences._

// Resolvers
resolvers ++= Seq(
  "dnvriend @ bintray" at "http://dl.bintray.com/dnvriend/maven"
)

// Dependencies
val rootDependencies = Seq(
  "com.typesafe.akka"   %% "akka-cluster"              % Vers.Akka,
  "com.typesafe.akka"   %% "akka-cluster-tools"        % Vers.Akka,
  "com.typesafe.akka"   %% "akka-cluster-sharding"     % Vers.Akka,
  "com.typesafe.akka"   %% "akka-persistence"          % Vers.Akka,
  "com.github.dnvriend" %% "akka-persistence-inmemory" % "1.1.5"
)

val testDependencies = Seq (
)

val dependencies =
  rootDependencies ++
  testDependencies

// Settings
//
val compileSettings = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:_",
  //"-language:existentials",
  //"-language:experimental.macros",
  //"-language:higherKinds",
  //"-language:implicitConversions",
  "-unchecked",
  //"-Xfatal-warnings",
  "-Xlint",
  "-Yno-adapted-args",
  //"-Ywarn-all",
  //"-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard"
)

val forkedJvmOption = Seq(
  "-server",
  "-Dfile.encoding=UTF8",
  "-Duser.timezone=GMT",
  "-Xss1m",
  "-Xms2048m",
  "-Xmx2048m",
  "-XX:+CMSClassUnloadingEnabled",
  "-XX:ReservedCodeCacheSize=256m",
  "-XX:+DoEscapeAnalysis",
  "-XX:+UseConcMarkSweepGC",
  "-XX:+UseParNewGC",
  "-XX:+UseCodeCacheFlushing",
  "-XX:+UseCompressedOops"
)

val formatting =
  FormattingPreferences()
    .setPreference(AlignParameters, true)
    .setPreference(AlignSingleLineCaseStatements, false)
    .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 40)
    .setPreference(CompactControlReadability, false)
    .setPreference(CompactStringConcatenation, false)
    .setPreference(DoubleIndentClassDeclaration, true)
    .setPreference(FormatXml, true)
    .setPreference(IndentLocalDefs, false)
    .setPreference(IndentPackageBlocks, true)
    .setPreference(IndentSpaces, 2)
    .setPreference(IndentWithTabs, false)
    .setPreference(MultilineScaladocCommentsStartOnFirstLine, false)
    .setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, false)
    .setPreference(PreserveSpaceBeforeArguments, false)
    .setPreference(RewriteArrowSymbols, false)
    .setPreference(SpaceBeforeColon, false)
    .setPreference(SpaceInsideBrackets, false)
    .setPreference(SpaceInsideParentheses, false)
    .setPreference(SpacesWithinPatternBinders, true)

val pluginsSettings =
  scalariformSettings

val settings = Seq(
  name := "postman",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.11.7",
  libraryDependencies ++= dependencies,
  fork in run := true,
  fork in Test := true,
  fork in testOnly := true,
  connectInput in run := true,
  javaOptions in run ++= forkedJvmOption,
  javaOptions in Test ++= forkedJvmOption,
  scalacOptions := compileSettings
  //,
  // formatting
  //
  //ScalariformKeys.preferences := formatting
)

val main =
  project
    .in(file("."))
    .settings(
      pluginsSettings ++ settings:_*
    )
