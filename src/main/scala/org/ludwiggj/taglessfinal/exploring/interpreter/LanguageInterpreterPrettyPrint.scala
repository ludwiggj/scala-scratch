package org.ludwiggj.taglessfinal.exploring.interpreter

import org.ludwiggj.taglessfinal.exploring.PrettyPrint
import org.ludwiggj.taglessfinal.exploring.bridge.ScalaToLanguageBridge
import org.ludwiggj.taglessfinal.exploring.language.Language

object LanguageInterpreterPrettyPrint {
  val interpret: Language[PrettyPrint] = new Language[PrettyPrint] {
    override def number(v: Int): PrettyPrint[Int] = s"($v)"

    override def increment(a: PrettyPrint[Int]): PrettyPrint[Int] = s"(inc $a)"

    override def add(a: PrettyPrint[Int], b: PrettyPrint[Int]): PrettyPrint[Int] = s"(+ $a $b)"

    override def text(v: String): PrettyPrint[String] = s"[$v]"

    override def toUpper(a: PrettyPrint[String]): PrettyPrint[String] = s"(toUpper $a)"

    override def concat(a: PrettyPrint[String], b: PrettyPrint[String]): PrettyPrint[String] = s"(concat $a $b)"

    override def toString(v: PrettyPrint[Int]): PrettyPrint[String] = s"(toString $v)"
  }
}
