package org.ludwiggj.taglessfinal.exploring.bridge

import org.ludwiggj.taglessfinal.exploring.language.LanguageExtended
import scala.language.higherKinds

trait ScalaToLanguageExtendedBridge[ScalaValue] {
  def apply[Wrapper[_]](implicit L: LanguageExtended[Wrapper]): Wrapper[ScalaValue]
}

object ScalaToLanguageExtendedBridge {
  def multiply(a: Int, b: Int): ScalaToLanguageExtendedBridge[Int] = new ScalaToLanguageExtendedBridge[Int] {
    override def apply[Wrapper[_]](implicit L: LanguageExtended[Wrapper]): Wrapper[Int] = {
      L.multiply(L.number(a), L.number(b))
    }
  }

  def buildComplexExpression(text: String, a: Int, b: Int): ScalaToLanguageExtendedBridge[String] =
    new ScalaToLanguageExtendedBridge[String] {
      override def apply[Wrapper[_]](implicit L: LanguageExtended[Wrapper]): Wrapper[String] = {
        val sum: Wrapper[Int] = L.multiply(L.add(L.number(a), L.increment(L.number(b))), L.increment(L.number(a)))
        L.concat(L.text(text), L.toString(sum))
      }
    }
}