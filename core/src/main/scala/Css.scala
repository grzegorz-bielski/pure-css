package pureframes.css

final class Css(val className: String, private[css] val styles: String):
  override def toString: String = className
