package pureframes.css

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import pureframes.css.internals.fs

object JSRender extends FileSystemRenderer:
  def toFiles(directory: String, renderers: StyleSheetRenderer*): Unit =
    js.Promise.all(
      renderers.map { renderer =>
        val path = s"$directory/${renderer.name}.css"
        fs.promises.writeFile(path, renderer.render)
      }.toJSArray
    )
