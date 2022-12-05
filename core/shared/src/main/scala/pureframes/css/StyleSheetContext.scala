package pureframes.css

import scala.collection.mutable

trait StyleSheetContext extends StyleSheetRenderer:
  def append(css: Css): Unit
  def name: String

object StyleSheetContext:
  def create: StyleSheetContext = create(None)
  def create(ctxName: String): StyleSheetContext = create(Some(ctxName))
  def create(ctxName: Option[String]): StyleSheetContext = new:
    val acc = mutable.Map.empty[String, Css]

    def name = ctxName.getOrElse("pure-css-stylesheet")
    def append(css: Css): Unit =
      acc.get(css.className) match
        case Some(_) =>
          throw new IllegalStateException(
            s"Generated className of ${css.className} was already processed. Consider taking the `css` macro out of functions or change them to inline functions."
          )
        case None =>
          acc.update(css.className, css)

    def render: String = acc.values.map { css =>
      s"${css.selector} {${css.styles}}"
    }.mkString
