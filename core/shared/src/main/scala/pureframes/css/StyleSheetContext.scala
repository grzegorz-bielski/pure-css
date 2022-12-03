package pureframes.css

import scala.collection.mutable

trait StyleSheetContext extends StyleSheetRenderer:
  def append(css: Css): Unit
  def name: String

object StyleSheetContext:
  def create: StyleSheetContext = create(None)
  def create(ctxName: String): StyleSheetContext = create(Some(ctxName))
  def create(ctxName: Option[String]): StyleSheetContext = new:
    val acc = mutable.Set.empty[Css]

    def name = ctxName.getOrElse("pure-css-stylesheet")
    def append(css: Css): Unit = acc.add(css)
    def render: String = acc.map { css =>
      s"${css.selector} {${css.styles}}"
    }.mkString
