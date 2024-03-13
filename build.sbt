import org.openurp.parent.Dependencies.*
import org.openurp.parent.Settings.*

ThisBuild / organization := "org.openurp.edu.course"
ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/openurp/edu-curricula"),
    "scm:git@github.com:openurp/edu-curricula.git"
  )
)

ThisBuild / developers := List(
  Developer(
    id = "chaostone",
    name = "Tihua Duan",
    email = "duantihua@gmail.com",
    url = url("http://github.com/duantihua")
  )
)

ThisBuild / description := "OpenURP Edu Curricula"
ThisBuild / homepage := Some(url("http://openurp.github.io/edu-curricula/index.html"))

val apiVer = "0.37.3"
val starterVer = "0.3.26"
val baseVer = "0.4.21-SNAPSHOT"
val openurp_edu_api = "org.openurp.edu" % "openurp-edu-api" % apiVer
val openurp_stater_web = "org.openurp.starter" % "openurp-starter-web" % starterVer
val openurp_stater_ws = "org.openurp.starter" % "openurp-starter-ws" % starterVer
val openurp_base_tag = "org.openurp.base" % "openurp-base-tag" % baseVer
lazy val root = (project in file("."))
  .settings()
  .aggregate(core, static, admin, index)

lazy val core = (project in file("core"))
  .settings(
    name := "openurp-edu-course-core",
    common,
    libraryDependencies ++= Seq(openurp_edu_api, beangle_webmvc_support, beangle_data_orm, beangle_ems_app)
  )

lazy val static = (project in file("static"))
  .settings(
    name := "openurp-edu-course-static",
    common
  )

lazy val admin = (project in file("admin"))
  .enablePlugins(WarPlugin, UndertowPlugin, TomcatPlugin)
  .settings(
    name := "openurp-edu-course-adminapp",
    common,
    libraryDependencies ++= Seq(openurp_stater_web, openurp_base_tag)
  ).dependsOn(core)

lazy val index = (project in file("index"))
  .enablePlugins(WarPlugin, UndertowPlugin, TomcatPlugin)
  .settings(
    name := "openurp-edu-course-indexapp",
    common,
    libraryDependencies ++= Seq(openurp_stater_web)
  ).dependsOn(core)

publish / skip := true
