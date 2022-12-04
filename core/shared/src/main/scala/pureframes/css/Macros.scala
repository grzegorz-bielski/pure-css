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
            Seq("/* PURE_CSS_START */" ++ sc.parts.head ++ "/* PURE_CSS_END */")
          else
            ("/* PURE_CSS_START */" ++ sc.parts.head) +: sc.parts.tail.init :+ (sc.parts.last ++ "/* PURE_CSS_END */")

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

private def Id: String =
  UUID.randomUUID.toString.replace("-", "").substring(0, 8)
