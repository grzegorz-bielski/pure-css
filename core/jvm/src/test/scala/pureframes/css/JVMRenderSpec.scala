package pureframes.css

import munit.FunSuite
import java.nio.file.Files
import java.nio.file.Paths

import StyledFixture.{given, *}

class RenderSpec extends FunSuite:
  val dir = "core/jvm/target"

  val renderers = Seq(
    FixtureStylesR,
    FixtureStylesGB,
    summon[StyleSheetContext]
  )

  override def beforeEach(context: BeforeEach) =
    renderers.foreach { r =>
      Files.deleteIfExists(Paths.get(s"${dir}/${r.name}.css"))
    }

  test("renders stylesheets") {
    JVMRender.toFiles(dir, renderers*)

    renderers
      .distinctBy(_.name)
      .foreach { renderer =>
        Files.exists(Paths.get(s"$dir/${renderer.name}"))
      }
  }
