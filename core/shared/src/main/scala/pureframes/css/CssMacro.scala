package pureframes.css

import java.util.UUID
import scala.quoted.*

import fastparse.parse
import cssparse.CssRulesParser
import com.helger.css.reader.CSSReader
import com.helger.css.ECSSVersion
import scala.collection.JavaConverters.*
import com.helger.css.reader.errorhandler.ThrowingCSSParseErrorHandler

def cssImpl(
    stringContext: Expr[StringContext],
    args: Expr[Seq[Arg]]
)(using Quotes): Expr[Css] =
  import quotes.reflect.*

  args match
    case Varargs(args) =>

      def parseCss(css: String) = 
        parse(css, CssRulesParser.declarationList(_))

      // // from: https://github.com/circe/circe/blob/series/0.14.x/modules/literal/src/main/scala-3/io/circe/literal/JsonLiteralMacros.scala
      val stringParts = stringContext match
        case '{ StringContext($parts: _*) } => parts.valueOrAbort
      val replacements = args.map(Replacement(stringParts, _))
      val cssString = stringParts.zip(replacements.map(_.placeholder)).foldLeft("") {
          case (acc, (part, placeholder)) =>
            val qm = "\""
            s"$acc$part$qm$placeholder$qm"
        } + stringParts.last
      val placeholderCssString = s".sel {$cssString}"

      // // println(cssString)
      // println(
      //   parseCss(cssString)
      // )

      val res = CSSReader.readFromString(placeholderCssString, ECSSVersion.CSS30, ThrowingCSSParseErrorHandler())

      if res == null then 
        println("parsed: null") 
        println(placeholderCssString)
      else println("parsed: " + res.getAllRules().asScala.map(_.getAsCSSString()))


      val parsedArgs = Varargs(
        args.map {
          case '{ $str: String } => '{ () => $str }
          case '{ $css: Css } =>
            Select.unique(css.asTerm, "_styles").asExprOf[Css.Thunk]
        }
      )

      val fileName = Position.ofMacroExpansion.sourceFile.name
      val className = Expr(s"${fileName.replace(".scala", "")}-$Id")

      val styleSheetCtx = Expr
        .summon[StyleSheetContext]
        .getOrElse(
          report.errorAndAbort("No given StyleSheetContext in the scope")
        )

      val stringCtx =
        val sc = stringContext.valueOrAbort

        val modifiedParts =
          if (sc.parts.length <= 0)
          then sc.parts
          else if (sc.parts.length == 1)
            Seq(pragmaStart ++ sc.parts.head ++ pragmaEnd)
          else
            (pragmaStart ++ sc.parts.head) +: sc.parts.tail.init :+ (sc.parts.last ++ pragmaEnd)

        Expr(StringContext(modifiedParts*))

      '{
        Css.create(
          $styleSheetCtx,
          $className,
          () => $stringCtx.s($parsedArgs.map(_.apply)*)
        )
      }

    case _ =>
      report.errorAndAbort(
        "Got unexpected arguments: " ++ args.show,
        args
      )

val pragmaStart = "/* PURE_CSS_START */"
val pragmaEnd = "/* PURE_CSS_END */"

private def Id: String =
  UUID.randomUUID.toString.replace("-", "").substring(0, 8)
