package pureframes.css

import scala.quoted.*

def cssMacro(
    sc: Expr[StringContext],
    args: Expr[Seq[String]]
)(using Quotes): Expr[Css] =
  import quotes.reflect.report
  import quotes.reflect.Position

  (sc, args) match
    case (Expr(sc), Expr(args)) =>
      val style = sc.s(args*)
      val fileName = Position.ofMacroExpansion.sourceFile.name
      val className = s"${fileName.replace(".scala", "")}-$Id"

      Expr(Css(className, style))
    case _ =>
      report.errorAndAbort(
        "Expected arguments known at compile time"
      )

private given ToExpr[Css] with
  def apply(css: Css)(using Quotes): Expr[Css] =
    val className = Expr(css.className)
    val styles = Expr(css.styles)

    '{ Css($className, $styles) }

private def Id: String =
  java.util.UUID.randomUUID.toString.replace("-", "").substring(0, 8)
