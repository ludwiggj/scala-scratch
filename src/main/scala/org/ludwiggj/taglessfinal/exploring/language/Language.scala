package org.ludwiggj.taglessfinal.exploring.language

import scala.language.higherKinds

// Language is the heart of our DSL. It defines what can we do with the hosted language.

// Language is parameterized by a Wrapper type which itself has an internal type. Both type parameters will be useful
// for us later. Wrapper will allow us to change the “package” on which we operate, while the type of the Wrapper makes
// sure our API is typesafe.
trait Language[Wrapper[_]] {
  def number(v: Int): Wrapper[Int]
  def increment(a: Wrapper[Int]): Wrapper[Int]
  def add(a: Wrapper[Int], b: Wrapper[Int]): Wrapper[Int]

  def text(v: String): Wrapper[String]
  def toUpper(a: Wrapper[String]): Wrapper[String]
  def concat(a: Wrapper[String], b: Wrapper[String]): Wrapper[String]

  // Method to create a Wrapper[X] values
  def toString(v: Wrapper[Int]): Wrapper[String]
}