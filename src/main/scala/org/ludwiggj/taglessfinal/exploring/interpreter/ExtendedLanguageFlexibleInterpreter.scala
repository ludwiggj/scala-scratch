package org.ludwiggj.taglessfinal.exploring.interpreter

import org.ludwiggj.taglessfinal.exploring.{NoWrap, PrettyPrint}
import org.ludwiggj.taglessfinal.exploring.bridge.{ScalaToExtendedLanguageBridge, ScalaToLanguageBridge}
import org.ludwiggj.taglessfinal.exploring.language.{ExtendedLanguage, Language}

import scala.language.higherKinds

class ExtendedLanguageFlexibleInterpreter[T[_]](l: Language[T], m: (T[Int], T[Int]) => T[Int]) extends ExtendedLanguage[T] {
  override def multiply(a: T[Int], b: T[Int]): T[Int] = m(a, b)

  override def number(v: Int): T[Int] = l.number(v)

  override def increment(a: T[Int]): T[Int] = l.increment(a)

  override def add(a: T[Int], b: T[Int]): T[Int] = l.add(a, b)

  override def text(v: String): T[String] = l.text(v)

  override def toUpper(a: T[String]): T[String] = l.toUpper(a)

  override def concat(a: T[String], b: T[String]): T[String] = l.concat(a, b)

  override def toString(v: T[Int]): T[String] = l.toString(v)
}

object ExtendedLanguageFlexibleInterpreter {
  def main(args: Array[String]): Unit = {

    val expr1: ScalaToLanguageBridge[String] = ScalaToLanguageBridge.buildComplexExpression("Result is", 10, 1)

    // multiply
    val expr2: ScalaToExtendedLanguageBridge[Int] = ScalaToExtendedLanguageBridge.multiply(3, 5)
    val expr3: ScalaToExtendedLanguageBridge[String] = ScalaToExtendedLanguageBridge.buildComplexExpression("Result is", 3, 5)

    def multiplyNoWrap(a: NoWrap[Int], b: NoWrap[Int]): NoWrap[Int] = a * b

    val interpreter = new ExtendedLanguageFlexibleInterpreter[NoWrap](LanguageInterpreter.interpret, multiplyNoWrap)

    def multiplyPrettyPrint(a: PrettyPrint[Int], b: PrettyPrint[Int]): PrettyPrint[Int] = s"(* $a $b)"

    val interpreterPrettyPrint = new ExtendedLanguageFlexibleInterpreter[PrettyPrint](
      LanguageInterpreterPrettyPrint.interpret, multiplyPrettyPrint
    )

    println(s"${expr1.apply(interpreterPrettyPrint)} = ${expr1.apply(interpreter)}")
    println(s"${expr2.apply(interpreterPrettyPrint)} = ${expr2.apply(interpreter)}")
    println(s"${expr3.apply(interpreterPrettyPrint)} = ${expr3.apply(interpreter)}")
  }
}