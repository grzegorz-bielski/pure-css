package pureframes.css.plugin

import dotty.tools.dotc.plugins.{PluginPhase, StandardPlugin}

final class Plugin extends StandardPlugin: 
    override val description = "Extracts css from scala sources"
    override val name = "pure-css-extractor-plugin"

    def init(options: List[String]): List[PluginPhase] = 
        println(s"$name called")
        Nil