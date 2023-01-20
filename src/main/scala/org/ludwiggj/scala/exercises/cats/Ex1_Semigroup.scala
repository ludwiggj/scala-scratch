package org.ludwiggj.scala.exercises.cats

import cats.Semigroup
import cats.implicits.catsSyntaxSemigroup

// (1) Semigroup
object Ex1_Semigroup {
  def main(args: Array[String]): Unit = {
    println(Semigroup[Int].combine(1, 2)) // 3

    println(Semigroup[List[Int]].combine(List(1, 2, 3), List(4, 5, 6))) // List(1, 2, 3, 4, 5, 6)
    println(Semigroup[Option[Int]].combine(Option(1), Option(2))) // Some(3)
    println(Semigroup[Option[Int]].combine(Option(1), None))      // Some(1)

    // Combining functions
    println(Semigroup[Int => Int].combine(_ + 1, _ * 10).apply(6)) // combine(6 + 1, 6 * 10) => 67
    println(Semigroup[String => String].combine(_.toUpperCase(), _.reverse).apply("hello")) // HELLOolleh

    // Combining maps
    println(Map("foo" -> Map("bar" -> 5)) ++ Map("foo" -> Map("bar" -> 6), "baz" -> Map())) // Map("foo" -> Map("bar" -> 6), "baz" -> Map())
    println(Map("foo" -> List(1, 2)) ++ Map("foo" -> List(3, 4), "bar" -> List(42)))        // Map("foo" -> List(3, 4), "bar" -> List(42))
    // vs.
    println(Map("foo" -> Map("bar" -> 5)).combine(Map("foo" -> Map("bar" -> 6), "baz" -> Map()))) // Map("foo" -> Map("bar" -> 11), "baz" -> Map())
    println(Map("foo" -> List(1, 2)).combine(Map("foo" -> List(3, 4), "bar" -> List(42))))        // Map("foo" -> List(1, 2, 3, 4), "bar" -> List(42))
  }
}
