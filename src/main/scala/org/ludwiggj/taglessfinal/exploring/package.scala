package org.ludwiggj.taglessfinal

package object exploring {
  // NoWrap defaults to the type it was parameterized with
  // The NoWrap type alias might be tricky to use in production code, due to its eager evaluation and other concerns.
  // Here we stick to it only for simplicity
  type NoWrap[ScalaValue] = ScalaValue

  type PrettyPrint[ScalaValue] = String
}
