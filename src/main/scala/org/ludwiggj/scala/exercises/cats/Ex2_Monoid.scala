package org.ludwiggj.scala.exercises.cats

import cats.implicits.toFoldableOps
import cats.kernel.Monoid

// (2) Monoid - combine
object Ex2_Monoid {
  def main(args: Array[String]): Unit = {
    println(Monoid[Map[String, Int]].combineAll(List(Map("a" -> 1, "b" -> 2), Map("a" -> 3)))) // Map("a" -> 4, "b" -> 2)
    println(Monoid[Map[String, Int]].combineAll(List())) // Map()

    // Fold
    val l = List(1, 2, 3, 4, 5)
    println(l.foldMap(identity)) // 15
    println(l.foldMap(i => i.toString)) // "12345"
    println(l.foldMap(i => (i, i.toString))) // (15, "12345")
  }
}
