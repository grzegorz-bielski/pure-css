package pureframes.css

import scala.scalajs.js
import pureframes.css.internals.fs
import js.JSConverters.*

import munit.FunSuite
import StyledFixture.{given, *}

class JSRenderSpec extends FunSuite:
  val dir = "core/js/target"
  val renderers = Seq(
    FixtureStylesR,
    FixtureStylesGB,
    summon[StyleSheetContext]
  )

  // TODO: this is messy, use fs2 just for tests?
  override def afterEach(context: AfterEach) =
    js.Promise.all(
      renderers
        .map(r => remove(s"${dir}/${r.name}.css"))
        .toJSArray
    )

  test("renders stylesheets") {
    JSRender.toFiles(dir, renderers*)

    renderers
      .distinctBy(_.name)
      .foreach { renderer =>
        exists(s"$dir/${renderer.name}")
      }
    // TODO: assert file contents exists
  }

def remove(path: String): js.Promise[Unit] =
  fs.promises
    .rm(
      path,
      new fs.RmOptions:
        force = true
        recursive = true
    )

def exists(path: String): js.Promise[Boolean] =
  fs.promises
    .access(path, fs.constants.R_OK)
    .`then`(_ => true)
    .`catch`(_ => false)
