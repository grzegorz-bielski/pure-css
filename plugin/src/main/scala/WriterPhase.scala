package pureframes.css.plugin

import scala.collection.mutable

import dotty.tools.dotc.plugins.PluginPhase
import dotty.tools.dotc.transform.Erasure
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.ast.tpd
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.Path
import java.nio.charset.StandardCharsets

final class WriterPhase(acc: Accumulator, path: String) extends PluginPhase:
  override val phaseName = Names.WriterPhase
  override def runsAfter: Set[String] = Set(Names.ExtractionPhase)

  override def prepareForUnit(tree: tpd.Tree)(using ctx: Context): Context =
    writeTo(Paths.get(path))
    ctx

  private def writeTo(path: Path): Unit =
    Files.write(path, styleSheet.getBytes(StandardCharsets.UTF_8))

  private def styleSheet: String =
    acc.current.toArray.foldLeft("") { case (z, (className, style)) =>
      z + s".${className}{${style.filterNot(_.isWhitespace)}}"
    }
