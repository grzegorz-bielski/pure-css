package pureframes.css

object StyleFixture:
    val color = "green"
    
    object FixtureStylesR extends Styles:
        val r = css"""
            color: red;
        """

    object FixtureStylesGB extends Styles:
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