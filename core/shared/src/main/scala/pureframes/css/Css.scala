package pureframes.css
final case class Css private[css] (
    className: String,
    private[css] _styles: CssThunk
):
  inline def selector: String = s".$className"
  override def toString: String = className
  
  private[css] lazy val styles: String = _styles()

type Thunk[T] = () => T
type CssThunk = Thunk[String]
