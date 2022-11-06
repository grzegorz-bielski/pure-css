package pureframes.css

import munit.FunSuite
import java.nio.file.Files
import java.nio.file.Paths

import StyleFixture.{given, *}

class RenderSpec extends FunSuite:
  test("renders stylesheets") {
    val dir = "core/jvm/target"

    val renderers = Seq(
      FixtureStylesR,
      FixtureStylesGB,
      summon[StyleSheetContext]
    )

    JVMRender.toFiles(dir, renderers*)

    renderers
      .distinctBy(_.name)
      .foreach { renderer =>
        Files.exists(Paths.get(s"$dir/${renderer.name}"))
      }
  }
