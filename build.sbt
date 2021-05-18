name := "gGiftNotifier"
organization := "info.genghis"
scalaVersion := "2.13.5"
version := "0.1.0"
licenses += "GPLv3" -> url("https://www.gnu.org/licenses/gpl-3.0.html")

val catsEffectVersion = "2.5.1"
val graalvmVersion = "21.1.0"
val http4sVersion = "0.21.22"
val jsoupVersion = "1.3.1"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.jsoup" % "jsoup" % jsoupVersion,
  "org.typelevel" %% "cats-effect" % catsEffectVersion,
  "org.graalvm.nativeimage" % "svm" % graalvmVersion % Provided
)

scalacOptions ++= Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-explaintypes", // Explain type errors in more detail.
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-language:existentials", // Existential types (besides wildcard types) can be written and inferred
  "-language:experimental.macros", // Allow macro definition (besides implementation and application)
  "-language:higherKinds", // Allow higher-kinded types
  "-language:implicitConversions", // Allow definition of implicit functions called views
  "-unchecked", // Enable additional warnings where generated code depends on assumptions.
  "-Xcheckinit", // Wrap field accessors to throw an exception on uninitialized access.
  "-Xfatal-warnings", // Fail the compilation if there are any warnings.
  "-Xlint:adapted-args", // Warn if an argument list is modified to match the receiver.
  "-Xlint:constant", // Evaluation of a constant arithmetic expression results in an error.
  "-Xlint:delayedinit-select", // Selecting member of DelayedInit.
  "-Xlint:doc-detached", // A Scaladoc comment appears to be detached from its element.
  "-Xlint:inaccessible", // Warn about inaccessible types in method signatures.
  "-Xlint:infer-any", // Warn when a type argument is inferred to be `Any`.
  "-Xlint:missing-interpolator", // A string literal appears to be missing an interpolator id.
  "-Xlint:nullary-unit", // Warn when nullary methods return Unit.
  "-Xlint:option-implicit", // Option.apply used implicit view.
  "-Xlint:package-object-classes", // Class or object defined in package object.
  "-Xlint:poly-implicit-overload", // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:private-shadow", // A private field (or class parameter) shadows a superclass field.
  "-Xlint:stars-align", // Pattern sequence wildcard must align with sequence component.
  "-Xlint:type-parameter-shadow", // A local type parameter shadows a type already in scope.
  "-Ywarn-dead-code", // Warn when dead code is identified.
  "-Ywarn-extra-implicit", // Warn when more than one implicit parameter section is defined.
  "-Ywarn-numeric-widen", // Warn when numerics are widened.
  "-Ywarn-unused:implicits", // Warn if an implicit parameter is unused.
  "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
  "-Ywarn-unused:locals", // Warn if a local definition is unused.
  "-Ywarn-unused:params", // Warn if a value parameter is unused.
  "-Ywarn-unused:patvars", // Warn if a variable bound in a pattern is unused.
  "-Ywarn-unused:privates", // Warn if a private member is unused.
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused.
  "-Ybackend-parallelism", "4", // Enable paralellisation — change to desired number!
  "-Ycache-plugin-class-loader:last-modified", // Enables caching of classloaders for compiler plugins
  "-Ycache-macro-class-loader:last-modified" // and macro definitions. This can lead to performance improvements.
)

// Note that the REPL can’t really cope with -Ywarn-unused:imports or -Xfatal-warnings so you should turn them off for the console.
Compile / console / scalacOptions --= Seq("-Ywarn-unused:imports", "-Xfatal-warnings")
Test / scalacOptions ++= Seq("-Yrangepos")

graalVMNativeImageOptions ++= Seq(
  "--allow-incomplete-classpath", // allow the image build with an incomplete class path. Report type resolution errors at runtime when they are accessed the first time, instead of during the image build.
  // "--enable-all-security-services", // add all security service classes to the generated image.
  // "--enable-http", // enable http support in the generated image
  // "--enable-https", // enable https support in the generated image
  // "--enable-url-protocols", // list of comma separated URL protocols to enable.
  // "--features", // a comma-separated list of fully qualified Feature implementation classes
  /*
  Each class can be initialized either (1) at run time, or (2) at build time.
  To specify class-initialization policies, two flags are provided:
    --initialize-at-build-time and --initialize-at-run-time.
  These flags allow specifying a policy for whole packages or individual classes.
  Refer: https://www.graalvm.org/reference-manual/native-image/ClassInitialization/#explicitly-specifying-class-initialization
   */
  "--initialize-at-build-time", // Without specifying classes or packages, the whole class hierarchy are initialized at build time
  "--initialize-at-run-time=scala.util.Random$", // `scala.util.Random$` depends on java.util.Random, which cannot be initialized at build time
  // "--install-exit-handlers", // provide java.lang.Terminator exit handlers for executable images
  "--libc=musl", // selects the libc implementation to use. Available implementations: glibc, musl, bionic
  "--no-fallback", // build stand-alone image or report failure
  // "--report-unsupported-elements-at-runtime", // report usage of unsupported methods and fields at run time when they are accessed the first time, instead of as an error during image building
  // "--shared", // build shared library
  "--static", // build a statically-linked executable (requires libc and zlib static libraries).
  // "--target", // selects native-image compilation target (in <OS>-<architecture> format). Defaults to host's OS-architecture pair.
  "--verbose", // enable verbose output
  "-J-Xms3072m", // Pass `-Xms3072m` directly to the JVM running the image generator
  "-J-Xmx6144m",
  "-H:+AddAllCharsets", // Make all hosted charsets available at run time.
  "-H:+ReportExceptionStackTraces", // Show exception stack traces for exceptions during image building.).
  // Find out all options at https://chriswhocodes.com/graalvm_native_image_ce_jdk11_options.html
)

// Except sbt-native-package, sbt-native-image is another option for building native image in Scala
enablePlugins(GraalVMNativeImagePlugin)

addCommandAlias("ni", "graalvm-native-image:packageBin")
