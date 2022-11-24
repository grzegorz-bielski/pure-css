package pureframes.css

import java.util.UUID
import scala.quoted.*

def cssImpl(
    sc: Expr[StringContext],
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

      val ctx = Expr
        .summon[StyleSheetContext]
        .getOrElse(
          report.errorAndAbort("No given StyleSheetContext in the scope")
        )

      '{
        Css.create($ctx, $className, () => $sc.s($parsedArgs.map(_.apply)*))
      }

    case _ =>
      report.errorAndAbort(
        "Expected arguments known at compile time. Got: " + args.show,
        args
      )

private def Id: String =
  UUID.randomUUID.toString.replace("-", "").substring(0, 8)
