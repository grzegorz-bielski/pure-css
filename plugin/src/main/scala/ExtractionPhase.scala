package pureframes.css.plugin

import scala.collection.mutable

import dotty.tools.dotc.plugins.PluginPhase
import dotty.tools.dotc.core.Contexts.Context
import dotty.tools.dotc.ast.tpd.*
import dotty.tools.dotc.core.StdNames.*
import dotty.tools.dotc.core.Names.*
import dotty.tools.dotc.core.Constants.Constant

final class ExtractionPhase(acc: Accumulator) extends PluginPhase:
  override val phaseName = Names.ExtractionPhase
  override def runsBefore: Set[String] = Set(Names.WriterPhase)
  override def runsAfter: Set[String] = Set("pickleQuotes")
  override def transformApply(tree: Apply)(using Context): Tree =
    // Extracts `style` during Css construction and replaces it with empty string
    tree match
      // format: off
      case Apply(select @ Select(New(Ident(name)), nme.CONSTRUCTOR), args) if name.show == "Css" =>
      // format: on
        val nextArgs = args match
          case
            // format: off
            (a @ Inlined(EmptyTree, Nil, Literal(Constant(className: String)))) ::
                 Inlined(EmptyTree, Nil, Literal(Constant(style: String)))      :: rest =>
            // format: on

            // add styles to accumulator so they can be generated in the WriterPhase 
            acc.push(className -> style)

            // remove styles from compiled sources
            val b = Inlined(EmptyTree, Nil, Literal(Constant("")))

            a :: b :: rest

          case _ => args

        Apply(select, nextArgs)

      case _ => tree
