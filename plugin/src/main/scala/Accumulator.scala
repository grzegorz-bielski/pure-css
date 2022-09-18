package pureframes.css.plugin

import scala.collection.mutable

final class Accumulator:
  @volatile
  private var acc = mutable.Map.empty[String, String]

  def push(kv: (String, String)): Unit = 
    acc += kv

  def current: mutable.Map[String, String] = acc
