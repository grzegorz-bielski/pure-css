package pureframes.css

final case class Css private[css] (
    className: String,
    _styles: Css.Thunk
):
  inline def selector: String = s".$className"
  override def toString: String = className

  private[css] lazy val styles: String = _styles()

object Css:
  type Thunk = () => String

  def create(
      ctx: StyleSheetContext,
      className: String,
      styles: Css.Thunk
  ): Css =
    val css = Css(className, styles)
    ctx.append(css)

    css
