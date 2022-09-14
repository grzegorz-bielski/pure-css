package pureframes.css.plugin

import dotty.tools.dotc.plugins.{PluginPhase, StandardPlugin}
import dotty.tools.dotc.*

final class Plugin extends StandardPlugin: 
    override val description = "Extracts css from scala sources"
    override val name = "pure-css-extractor-plugin"

    def init(options: List[String]): List[PluginPhase] = 
        println(s"$name called")
        Nil


final class CollectionPhase extends PluginPhase:
    override val phaseName = getClass.getSimpleName
    override def runsAfter: Set[String] = Set("pickleQuotes")
    //https://www.youtube.com/watch?v=oqYd_Lwj2p0