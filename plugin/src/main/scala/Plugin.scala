package pureframes.css.plugin

import scala.collection.mutable
import dotty.tools.dotc.plugins.*

final class Plugin extends StandardPlugin:
  override val description = "Extracts css from scala sources"
  override val name = "pure-css"

  def init(ops: List[String]): List[PluginPhase] =
    println(s"$name called")
    val options = Options.fromCLI(ops)
    val acc = Accumulator()

    List(ExtractionPhase(acc), WriterPhase(acc, options.path))

private[plugin] final class Options(
    private val ops: Map[String, String]
):
  def path: String =
    ops.get("path").getOrElse("kek")

private[plugin] object Options:
  def fromCLI(options: List[String]): Options =
    Options(
      options
        .map(_.split(":"))
        .collect { case Array(k, v) => k -> v }
        .toMap
    )
