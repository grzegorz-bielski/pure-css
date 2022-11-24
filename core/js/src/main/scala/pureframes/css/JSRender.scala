package pureframes.css

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import pureframes.css.internals.fs

object JSRender extends FileSystemRenderer:
  def toFiles(directory: String, renderers: StyleSheetRenderer*): Unit =
    if js.typeOf(js.Dynamic.global.window) == "undefined" then
      js.dynamicImport {
        println("Generating stylesheets")

        collectStyles(directory, renderers)
          .foreach(fs.promises.writeFile(_, _))
      }
    else println("Running in browser - aborting stylesheet generation")
