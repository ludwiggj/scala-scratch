package org.ludwiggj.taglessfinal.exploring.interpreter

import org.ludwiggj.taglessfinal.exploring.NoWrap
import org.ludwiggj.taglessfinal.exploring.bridge.{ScalaToExtendedLanguageBridge, ScalaToLanguageBridge}
import org.ludwiggj.taglessfinal.exploring.language.ExtendedLanguage

object ExtendedLanguageInterpreter {
  // This interpreter has an extra method: multiply. It requires a bit of writing.
  // This could be less if we delegate/inherit - see delegation example below.

  // The good part is that at no point do we touch the Language code.
  val interpret: ExtendedLanguage[NoWrap] = new ExtendedLanguage[NoWrap] {
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

object ExtendedLanguageInterpreterExample {
  def main(args: Array[String]): Unit = {

    val expr1: ScalaToLanguageBridge[String] = ScalaToLanguageBridge.buildComplexExpression("Result is", 10, 1)

    // multiply
    val expr2: ScalaToExtendedLanguageBridge[Int] = ScalaToExtendedLanguageBridge.multiply(3, 5)
    val expr3: ScalaToExtendedLanguageBridge[String] = ScalaToExtendedLanguageBridge.buildComplexExpression("Result is", 3, 5)

    println(expr1.apply(ExtendedLanguageInterpreter.interpret))
    println(expr2.apply(ExtendedLanguageInterpreter.interpret))
    println(expr3.apply(ExtendedLanguageInterpreter.interpret))
  }
}