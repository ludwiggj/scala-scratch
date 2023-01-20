package org.ludwiggj.taglessfinal.exploring.interpreter.extended

import org.ludwiggj.taglessfinal.exploring.NoWrap
import org.ludwiggj.taglessfinal.exploring.language.LanguageExtended

object LanguageExtendedInterpreterNoWrap {
  // This interpreter has an extra method: multiply. It requires a bit of writing.
  // This could be less if we delegate/inherit - see delegation example below.

  // The good part is that at no point do we touch the Language code.
  val interpret: LanguageExtended[NoWrap] = new LanguageExtended[NoWrap] {
    override def multiply(a: NoWrap[Int], b: NoWrap[Int]): NoWrap[Int] = a * b

    override def number(v: Int): NoWrap[Int] = v

    override def increment(a: NoWrap[Int]): NoWrap[Int] = a + 1

    override def add(a: NoWrap[Int], b: NoWrap[Int]): NoWrap[Int] = a + b

    override def text(v: String): NoWrap[String] = v

    override def toUpper(a: NoWrap[String]): NoWrap[String] = a.toUpperCase

    override def concat(a: NoWrap[String], b: NoWrap[String]): NoWrap[String] = a + " " + b

    override def toString(v: NoWrap[Int]): NoWrap[String] = v.toString
  }
}
