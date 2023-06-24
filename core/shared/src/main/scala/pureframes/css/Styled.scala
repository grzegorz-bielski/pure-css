package pureframes.css
import pureframes.css.cssImpl

type Arg = Css | String

trait Styled extends StyleSheetRenderer:
  final given StyleSheetContext = StyleSheetContext.create
  final lazy val render: String = summon[StyleSheetContext].render

  final val name: String = summon[StyleSheetContext].name

extension (inline sc: StringContext)
  inline def css(inline args: Arg*): Css =
    ${ cssImpl('sc, 'args) }
