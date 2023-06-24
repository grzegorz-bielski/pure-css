# pure-css

**Experimental** library for embedding CSS in Scala sources.

## Usage

See `StyleExtractionSpec.scala`

### Dev notes
- parsing functionality: 
    - cssparse: https://github.com/com-lihaoyi/fastparse/tree/master/cssparse
    -  needs some glue code to support validation of:
        - nested selectors
        - checking if rules are valid might be bothersome
    - ph-css: https://github.com/phax/ph-css
        - inflexible, for example doesn't support any nesting out of the box
- collect stylesheets automagically: https://github.com/Lumintorious/Scala-3-Macros-Deep-Dive/blob/main/2.%20PackageWideSearch.md
    - flawed because of the macro cache - the root style collection could reference stale stylesheets
- vite plugin for css extraction
    - weird setup, but it works
    - prototype at pure-tetrio