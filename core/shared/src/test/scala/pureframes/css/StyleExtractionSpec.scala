package pureframes.css.tests

import pureframes.css.{given, *}
import munit.FunSuite

class StyleExtractionSpec extends FunSuite:
  test("generates dynamic class names once for the same symbol") {
    given StyleSheetContext = StyleSheetContext.create

    val stylesA = getStyles("12rem")

    assertEquals(
      stylesA.className,
      stylesA.className
    )

    assertNotEquals(
      stylesA.className,
      getStyles("12rem").className
    )
  }

  test("returns the same css class name on different access methods") {
    object TestStyles extends Styled:
      inline val color = "tomato"
      val styles = css"""
          color: $color;
      """

    val uses = List(
      TestStyles.styles.className,
      s"${TestStyles.styles}",
      TestStyles.styles.toString
    )

    assert(
      uses.forall(_ == uses.head)
    )
  }

  test("works with nested interpolation") {
    object TestStyles extends Styled:
      inline val size = "12rem"

      val header =
        css"""
          color: #3659e2;
          margin-bottom: $size;
        """

      val betterHeader =
        css"""
          ${header}
          font-weight: bold;
          width: $size;
        """

    assert(!TestStyles.render.isEmpty)
  }

  test("generates correct pragmas") {
    given StyleSheetContext = StyleSheetContext.create

    val styles = css"""
      position: absolute;
      bottom: 0;
      right: 0;

      button {
          color: tomato;
      }
    """

    val rendered = styles._styles()

    assert(rendered.contains(pragmaStart))
    assert(rendered.contains(pragmaEnd))
  }

  inline def getStyles(fontSize: String): Css =
    css"""
        font-size: ${fontSize};
    """
