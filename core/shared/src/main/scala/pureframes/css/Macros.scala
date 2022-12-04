package pureframes.css

import java.util.UUID
import scala.quoted.*

def cssImpl(
    stringContext: Expr[StringContext],
    args: Expr[Seq[Arg]]
)(using Quotes): Expr[Css] =
  import quotes.reflect.*

  args match
    case Varargs(args) =>
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
