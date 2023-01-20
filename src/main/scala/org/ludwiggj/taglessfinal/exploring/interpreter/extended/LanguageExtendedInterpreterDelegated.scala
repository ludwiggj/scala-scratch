package org.ludwiggj.taglessfinal.exploring.interpreter.extended

import org.ludwiggj.taglessfinal.exploring.language.{Language, LanguageExtended}

class LanguageExtendedInterpreterDelegated[T[_]](l: Language[T], m: (T[Int], T[Int]) => T[Int]) extends LanguageExtended[T] {
  override def multiply(a: T[Int], b: T[Int]): T[Int] = m(a, b)

  // all delegated
  override def number(v: Int): T[Int] = l.number(v)

  override def increment(a: T[Int]): T[Int] = l.increment(a)

  override def add(a: T[Int], b: T[Int]): T[Int] = l.add(a, b)

  override def text(v: String): T[String] = l.text(v)

  override def toUpper(a: T[String]): T[String] = l.toUpper(a)

  override def concat(a: T[String], b: T[String]): T[String] = l.concat(a, b)

  override def toString(v: T[Int]): T[String] = l.toString(v)
}
