package org.ludwiggj.taglessfinal.exploring.workout

import org.ludwiggj.taglessfinal.exploring.{NoWrap, PrettyPrint}
import org.ludwiggj.taglessfinal.exploring.bridge.{ScalaToLanguageBridge, ScalaToLanguageExtendedBridge}
import org.ludwiggj.taglessfinal.exploring.interpreter.{LanguageInterpreterNoWrap, LanguageInterpreterPrettyPrint}
import org.ludwiggj.taglessfinal.exploring.interpreter.extended.{LanguageExtendedInterpreterDelegated, LanguageExtendedInterpreterNoWrap, LanguageExtendedInterpreterNoWrapDelegated}

object LanguageExtendedInterpreterWorkout {
  def main(args: Array[String]): Unit = {

    val expr1: ScalaToLanguageBridge[String] = ScalaToLanguageBridge.buildComplexExpression("Result is", 10, 1)

    // multiply
    val expr2: ScalaToLanguageExtendedBridge[Int] = ScalaToLanguageExtendedBridge.multiply(3, 5)
    val expr3: ScalaToLanguageExtendedBridge[String] = ScalaToLanguageExtendedBridge.buildComplexExpression("Result is", 3, 5)

    println(expr1.apply(LanguageExtendedInterpreterNoWrap.interpret))
    println(expr2.apply(LanguageExtendedInterpreterNoWrap.interpret))
    println(expr3.apply(LanguageExtendedInterpreterNoWrap.interpret))

    // no wrap delegate
    val interpreter1: LanguageExtendedInterpreterNoWrapDelegated = new LanguageExtendedInterpreterNoWrapDelegated(
      LanguageInterpreterNoWrap.interpret
    )

    println(expr1.apply(interpreter1))
    println(expr2.apply(interpreter1))
    println(expr3.apply(interpreter1))

    // generic wrap delegate
    def multiplyNoWrap(a: NoWrap[Int], b: NoWrap[Int]): NoWrap[Int] = a * b

    val interpreter: LanguageExtendedInterpreterDelegated[NoWrap] = new LanguageExtendedInterpreterDelegated[NoWrap](
      LanguageInterpreterNoWrap.interpret,
      multiplyNoWrap
    )

    def multiplyPrettyPrint(a: PrettyPrint[Int], b: PrettyPrint[Int]): PrettyPrint[Int] = s"(* $a $b)"

    val interpreterPrettyPrint: LanguageExtendedInterpreterDelegated[PrettyPrint] = new LanguageExtendedInterpreterDelegated[PrettyPrint](
      LanguageInterpreterPrettyPrint.interpret,
      multiplyPrettyPrint
    )

    println(s"${expr1.apply(interpreterPrettyPrint)} = ${expr1.apply(interpreter)}")
    println(s"${expr2.apply(interpreterPrettyPrint)} = ${expr2.apply(interpreter)}")
    println(s"${expr3.apply(interpreterPrettyPrint)} = ${expr3.apply(interpreter)}")

  }
}