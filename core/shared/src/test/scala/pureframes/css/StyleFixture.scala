package pureframes.css

object StyledFixture:
  inline val color = "green"

  object FixtureStylesR extends Styled:
    val r = css"""
        color: red;
    """

  object FixtureStylesGB extends Styled:
    val g = css"""
        color: $color;
    """

    val b = css"""
        color: black;
    """

  given StyleSheetContext = StyleSheetContext.create("given")

  val a = css"""
        color: tomato;
    """
