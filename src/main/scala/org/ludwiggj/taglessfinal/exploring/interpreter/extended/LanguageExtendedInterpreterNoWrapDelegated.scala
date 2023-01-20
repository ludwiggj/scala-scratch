package org.ludwiggj.taglessfinal.exploring.interpreter.extended

import org.ludwiggj.taglessfinal.exploring.NoWrap
import org.ludwiggj.taglessfinal.exploring.bridge.{ScalaToLanguageExtendedBridge, ScalaToLanguageBridge}
import org.ludwiggj.taglessfinal.exploring.interpreter.LanguageInterpreterNoWrap
import org.ludwiggj.taglessfinal.exploring.language.{LanguageExtended, Language}

class LanguageExtendedInterpreterNoWrapDelegated(l: Language[NoWrap]) extends LanguageExtended[NoWrap] {
  override def multiply(a: NoWrap[Int], b: NoWrap[Int]): NoWrap[Int] = a * b

  // Following methods delegate
  override def number(v: Int): NoWrap[Int] = l.number(v)

  override def increment(a: NoWrap[Int]): NoWrap[Int] = l.increment(a)

  override def add(a: NoWrap[Int], b: NoWrap[Int]): NoWrap[Int] = l.add(a, b)

  override def text(v: String): NoWrap[String] = l.text(v)

  override def toUpper(a: NoWrap[String]): NoWrap[String] = l.toUpper(a)

  override def concat(a: NoWrap[String], b: NoWrap[String]): NoWrap[String] = l.concat(a, b)

  override def toString(v: NoWrap[Int]): NoWrap[String] = l.toString(v)
}
