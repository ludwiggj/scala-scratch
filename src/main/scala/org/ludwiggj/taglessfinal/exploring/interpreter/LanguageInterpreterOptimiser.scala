package org.ludwiggj.taglessfinal.exploring.interpreter

import org.ludwiggj.taglessfinal.exploring.bridge.ScalaToLanguageBridge
import org.ludwiggj.taglessfinal.exploring.language.Language

object OptimisingLanguageInterpreter {
  type Nested[ScalaValue] = ScalaToLanguageBridge[ScalaValue]

  val simplify: Language[Nested] = new Language[Nested] {
    var nesting = 0

    override def number(v: Int): Nested[Int] = new ScalaToLanguageBridge[Int] {
      override def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[Int] = {
        if (nesting > 0) {
          val temp = nesting
          nesting = 0
          L.add(L.number(temp), L.number(v))
        } else {
          L.number(v)
        }
      }
    }

    override def increment(a: Nested[Int]): Nested[Int] = new ScalaToLanguageBridge[Int] {
      override def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[Int] = {
        nesting = nesting + 1
        a.apply(L)
      }
    }

    override def add(a: Nested[Int], b: Nested[Int]): Nested[Int] = new Nested[Int] {
      override def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[Int] = {
        if (nesting > 0) {
          val temp = nesting
          nesting = 0
          L.add(L.number(temp), L.add(a.apply(L), b.apply(L)))
        } else {
          L.add(a.apply(L), b.apply(L))
        }
      }
    }

    override def text(v: String): Nested[String] = null

    override def toUpper(a: Nested[String]): Nested[String] = null

    override def concat(a: Nested[String], b: Nested[String]): Nested[String] = null

    override def toString(v: Nested[Int]): Nested[String] = null
  }
}

object OptimisingLanguageInterpreterExample {


  def main(args: Array[String]): Unit = {
      }
}