package pureframes.css

type Arg = Css | String

trait Styles extends StyleSheetRenderer:
  final given StyleSheetContext = StyleSheetContext.create
  final lazy val render: String = summon[StyleSheetContext].render

extension (inline sc: StringContext)
  inline def css(inline args: Arg*): Css =
    ${ cssImpl('sc, 'args) }
