package pureframes.css

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.Path
import java.nio.charset.StandardCharsets

object JVMRender extends FileSystemRenderer:
  def toFiles(directory: String, renderers: StyleSheetRenderer*): Unit =
    renderers.foreach { renderer =>
      val path = Paths.get(s"$directory/${renderer.name}.css")
      writeTo(path, renderer.render)
    }

  private def writeTo(path: Path, content: String): Unit =
    Files.write(path, content.getBytes(StandardCharsets.UTF_8))
