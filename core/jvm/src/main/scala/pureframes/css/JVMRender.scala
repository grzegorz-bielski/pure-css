package pureframes.css

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.Path
import java.nio.charset.StandardCharsets

object JVMRender extends FileSystemRenderer:
  def toFiles(directory: String, renderers: StyleSheetRenderer*): Unit =
    collectStyles(directory, renderers)
      .foreach(writeTo(_, _))

  private def writeTo(path: String, content: String): Unit =
    Files.write(Paths.get(path), content.getBytes(StandardCharsets.UTF_8))
