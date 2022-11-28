package org.ludwiggj.taglessfinal.exploring.bridge

import org.ludwiggj.taglessfinal.exploring.language.Language

import scala.language.higherKinds

// With a Language in place, we now need to bridge the gap between the hosting language (Scala) and our hosted language.
// Here we will build an interface for a generic bridge to do just that
// It takes an implicit Language to build expressions and after calling apply returns a result wrapped in the desired
// Wrapper class.
trait ScalaToLanguageBridge[ScalaValue] {
  def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[ScalaValue]
}

object ScalaToLanguageBridge {
  // Our bridges are really simple. They take a single Scala value number: Int and convert it into an expression in our
  // language. Since we are operating only on given L we cannot represent incorrect logic, like incrementing a String.
  // This approach for bridges is “fine grained” – we build only simple expressions.
  def buildNumber(number: Int): ScalaToLanguageBridge[Int] = new ScalaToLanguageBridge[Int] {
    override def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[Int] = L.number(number)
  }

  def buildIncrementNumber(number: Int): ScalaToLanguageBridge[Int] = new ScalaToLanguageBridge[Int] {
    override def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[Int] = L.increment(L.number(number))
  }

  // But can easily combine those simple expression into bigger ones
  def buildIncrementExpression(expression: ScalaToLanguageBridge[Int]): ScalaToLanguageBridge[Int] =
    new ScalaToLanguageBridge[Int] {
      override def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[Int] = L.increment(expression.apply)
    }

  // Or just follow a “coarse grained” approach where we express bigger algorithms straight away

  // builds an expression like: println(s"$text ${a + (b + 1)}")
  def buildComplexExpression(text: String, a: Int, b: Int): ScalaToLanguageBridge[String] =
    new ScalaToLanguageBridge[String] {
      override def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[String] = {
        val addition: Wrapper[Int] = L.add(L.number(a), L.increment(L.number(b)))
        L.concat(L.text(text), L.toString(addition))
      }
    }

  def buildIncrementExpression(): ScalaToLanguageBridge[Int] = new ScalaToLanguageBridge[Int] {
    override def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[Int] = {
      L.increment(L.add(L.number(10), L.increment(L.increment(L.increment(L.number(3))))))
//      L.add(L.number(10), L.increment(L.increment(L.increment(L.number(0)))))
//      L.increment(L.increment(L.increment(L.number(0))))
//      L.increment(L.number(5))
//      L.number(0)
    }
  }
}
