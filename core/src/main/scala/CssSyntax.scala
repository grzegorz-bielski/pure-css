package pureframes.css

extension (inline sc: StringContext)
  inline def css(inline args: String*): Css = 
    ${ cssMacro('sc, 'args) }
  