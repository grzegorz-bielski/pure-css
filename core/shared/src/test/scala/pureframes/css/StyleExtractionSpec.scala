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

  test("func style extraction") {
    given StyleSheetContext = StyleSheetContext.create

    def rotation(direction: "up" | "left" | "right" | "down") =
      val rotation =
        direction match
          case d: "up"    => "-90"
          case d: "right" => "0"
          case d: "down"  => "45"
          case d: "left"  => "180"

      css"""
        transform: rotate(${rotation}deg);
      """

    val rotateLeft = rotation("left")
    assert(rotateLeft.styles.contains("180"))

    intercept[IllegalStateException] {
      // `css` ,acro is expanded only once in the `rotation` method body
      // but subsequent calls to `rotation` produced different styles with the same `className`
      rotation("right")
    }
  }

  test("inline func style extraction") {
    given StyleSheetContext = StyleSheetContext.create

    inline def rotation(direction: "up" | "left" | "right" | "down") =
      val rotation =
        inline direction match
          case d: "up"    => "-90"
          case d: "right" => "0"
          case d: "down"  => "45"
          case d: "left"  => "180"

      css"""
        transform: rotate(${rotation}deg);
      """

    val all = List(
      rotation("left"),
      rotation("right"),
      rotation("down"),
      rotation("up")
    )

    assert(all(0).styles.contains("180"))
    assert(all(1).styles.contains("0"))
    assert(all(2).styles.contains("45"))
    assert(all(3).styles.contains("-90"))

    assert(
      // inline def expands to multiple lines, so `css` macro is expanded multiple times and produces different `className`s
      all.distinctBy(_.className).length == all.length
    )
  }

  inline def getStyles(fontSize: String): Css =
    css"""
        font-size: ${fontSize};
    """
