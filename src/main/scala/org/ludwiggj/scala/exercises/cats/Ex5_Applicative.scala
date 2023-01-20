package org.ludwiggj.scala.exercises.cats

import cats.Applicative

// (5) Applicative - pure
object Ex5_Applicative {
  def main(args: Array[String]): Unit = {
    println(Applicative[Option].pure(1)) // Some(1)

    // Compose
    println((Applicative[List] compose Applicative[Option]).pure(1)) // List(Some(1))
    println((Applicative[Option] compose Applicative[List]).pure(1)) // Some(List(1))

    // APPLICATIVE FUNCTORS & MONADS
    //
    // Applicative is a generalization of Monad, allowing expression of effectful computations in a pure functional way.
    // Applicative is generally preferred to Monad when the structure of a computation is fixed a priori. That makes it
    // possible to perform certain kinds of static analysis on applicative values.
    // a priori = relating to or denoting reasoning or knowledge which proceeds from theoretical deduction rather than
    // from observation or experience.
  }
}
