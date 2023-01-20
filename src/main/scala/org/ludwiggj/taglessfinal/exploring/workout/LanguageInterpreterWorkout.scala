package org.ludwiggj.taglessfinal.exploring.workout

import org.ludwiggj.taglessfinal.exploring.bridge.ScalaToLanguageBridge
import org.ludwiggj.taglessfinal.exploring.interpreter.OptimisingLanguageInterpreter.Nested
import org.ludwiggj.taglessfinal.exploring.interpreter.{LanguageInterpreterNoWrap, LanguageInterpreterPrettyPrint, OptimisingLanguageInterpreter}

object LanguageInterpreterWorkout {
  def main(args: Array[String]): Unit = {
    val expr1: ScalaToLanguageBridge[String] = ScalaToLanguageBridge.buildComplexExpression("Result is", 10, 1)

    println(s"Result      (no wrap): ${expr1.apply(LanguageInterpreterNoWrap.interpret)}")
    println(s"Result (pretty print): ${expr1.apply(LanguageInterpreterPrettyPrint.interpret)}")

    val incrementExpression: ScalaToLanguageBridge[Int] = ScalaToLanguageBridge.buildIncrementExpression()
    val simplifiedExpression: Nested[Int] = incrementExpression.apply(OptimisingLanguageInterpreter.simplify)

    println(s"Unoptimised ${incrementExpression.apply(LanguageInterpreterPrettyPrint.interpret)} = ${incrementExpression.apply(LanguageInterpreterNoWrap.interpret)}")
    println(s"Optimised ${simplifiedExpression.apply(LanguageInterpreterPrettyPrint.interpret)} = ${simplifiedExpression.apply(LanguageInterpreterNoWrap.interpret)}")
  }
}