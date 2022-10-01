package pureframes.css

import scala.scalajs.js
import pureframes.css.internals.fs

import munit.FunSuite
import StyleFixture.{given, *}

class JSRenderSpec extends FunSuite:
  test("renders stylesheets") {
    val dir = "core/js/target"

    val renderers = Seq(
      FixtureStylesR,
      FixtureStylesGB,
      summon[StyleSheetContext]
    )

    Render.toFiles(dir, renderers*)

    renderers
      .distinctBy(_.name)
      .foreach { renderer =>
        exists(s"$dir/${renderer.name}")
      }
  }

def exists(path: String): js.Promise[Boolean] =
  fs.promises
    .access(path, fs.constants.R_OK)
    .`then`(_ => true)
    .`catch`(_ => false)
