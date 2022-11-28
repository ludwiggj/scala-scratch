package org.ludwiggj.taglessfinal.exploring.interpreter

import org.ludwiggj.taglessfinal.exploring.NoWrap
import org.ludwiggj.taglessfinal.exploring.bridge.{ScalaToExtendedLanguageBridge, ScalaToLanguageBridge}
import org.ludwiggj.taglessfinal.exploring.language.{ExtendedLanguage, Language}

class ExtendedLanguageDelegateInterpreter(l: Language[NoWrap]) extends ExtendedLanguage[NoWrap] {
  override def multiply(a: NoWrap[Int], b: NoWrap[Int]): NoWrap[Int] = a * b

  override def number(v: Int): NoWrap[Int] = l.number(v)

  override def increment(a: NoWrap[Int]): NoWrap[Int] = l.increment(a)

  override def add(a: NoWrap[Int], b: NoWrap[Int]): NoWrap[Int] = l.add(a, b)

  override def text(v: String): NoWrap[String] = l.text(v)

  override def toUpper(a: NoWrap[String]): NoWrap[String] = l.toUpper(a)

  override def concat(a: NoWrap[String], b: NoWrap[String]): NoWrap[String] = l.concat(a, b)

  override def toString(v: NoWrap[Int]): NoWrap[String] = l.toString(v)
}

object ExtendedLanguageDelegateInterpreterExample {
  def main(args: Array[String]): Unit = {
    val expr1: ScalaToLanguageBridge[String] = ScalaToLanguageBridge.buildComplexExpression("Result is", 10, 1)

    // multiply
    val expr2: ScalaToExtendedLanguageBridge[Int] = ScalaToExtendedLanguageBridge.multiply(3, 5)
    val expr3: ScalaToExtendedLanguageBridge[String] = ScalaToExtendedLanguageBridge.buildComplexExpression("Result is", 3, 5)

    val interpret = new ExtendedLanguageDelegateInterpreter(LanguageInterpreter.interpret)

    println(expr1.apply(interpret))
    println(expr2.apply(interpret))
    println(expr3.apply(interpret))
  }
}
