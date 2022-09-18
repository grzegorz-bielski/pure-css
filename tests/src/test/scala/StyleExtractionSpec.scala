package pureframes.css.tests

import pureframes.css.*
import munit.FunSuite

class StyleExtractionSpec extends FunSuite {
  test("generates dynamic class names once for the same symbol") {
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

  test("removes embedded styles") {
    assertEquals(
      css"""
            display: inline-flex;
        """.styles,
      ""
    )
  }

  test("returns the same css class name on different access methods") {
    inline val color = "tomato"
    val styles = css"""
            color: $color;
        """

    val uses = List(
      styles.className,
      s"$styles",
      styles.toString
    )

    assert(
      uses.forall(_ == uses.head)
    )
  }

  inline def getStyles(fontSize: String): Css =
    css"""
        font-size: ${fontSize};
    """
}
