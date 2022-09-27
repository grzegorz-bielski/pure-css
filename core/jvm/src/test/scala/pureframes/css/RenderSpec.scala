package pureframes.css.tests

import munit.FunSuite

import pureframes.css.Render
import pureframes.css.StyleSheetContext

import pureframes.css.tests.*

class RenderSpec extends FunSuite:
  test("collects provided styles") {

    Render.toFile(
      "core/jvm/target/stylesheet.css",
      FixtureStylesR,
      FixtureStylesGB,
      summon[StyleSheetContext]
    )

    println("run")
  }
