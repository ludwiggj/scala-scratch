package org.ludwiggj.taglessfinal.exploring.language

import scala.language.higherKinds

// Language has one math operation – add, but let’s say we want another one called multiply.
// If we add the new method to Language it will cause a ripple effect throughout the system.
// Every interpreter that uses the language would need to be updated.

// Instead we create a child language, which has the API of its parent, and the new multiply method
trait LanguageExtended[Wrapper[_]] extends Language[Wrapper] {
  def multiply(a: Wrapper[Int], b: Wrapper[Int]): Wrapper[Int]
}