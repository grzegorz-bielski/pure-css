package pureframes.css

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.Path
import java.nio.charset.StandardCharsets

object Render:
    def toFile(path: String, renderers: StyleSheetRenderer*): Unit = 
        val content = renderers.map(_.render).mkString
        writeTo(Paths.get(path), content)

    private def writeTo(path: Path, content: String): Unit =
        Files.write(path, content.getBytes(StandardCharsets.UTF_8))