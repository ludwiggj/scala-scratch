package org.ludwiggj.taglessfinal.exploring.bridge

import org.ludwiggj.taglessfinal.exploring.language.ExtendedLanguage
import scala.language.higherKinds

trait ScalaToExtendedLanguageBridge[ScalaValue] {
  def apply[Wrapper[_]](implicit L: ExtendedLanguage[Wrapper]): Wrapper[ScalaValue]
}

object ScalaToExtendedLanguageBridge {
  def multiply(a: Int, b: Int): ScalaToExtendedLanguageBridge[Int] = new ScalaToExtendedLanguageBridge[Int] {
    override def apply[Wrapper[_]](implicit L: ExtendedLanguage[Wrapper]): Wrapper[Int] = {
      L.multiply(L.number(a), L.number(b))
    }
  }

  def buildComplexExpression(text: String, a: Int, b: Int): ScalaToExtendedLanguageBridge[String] =
    new ScalaToExtendedLanguageBridge[String] {
      override def apply[Wrapper[_]](implicit L: ExtendedLanguage[Wrapper]): Wrapper[String] = {
        val sum: Wrapper[Int] = L.multiply(L.add(L.number(a), L.increment(L.number(b))), L.increment(L.number(a)))
        L.concat(L.text(text), L.toString(sum))
      }
    }
}