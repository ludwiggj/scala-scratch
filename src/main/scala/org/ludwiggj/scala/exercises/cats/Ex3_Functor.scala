package org.ludwiggj.scala.exercises.cats

import cats.Functor

// (3) Functor
object Ex3_Functor {
  def main(args: Array[String]): Unit = {
    // def map[A, B](fa: F[A])(f: A => B): F[B]

    // lift
    val lenOption: Option[String] => Option[Int] = Functor[Option].lift(_.length)
    println(lenOption(Some("abcd"))) // Some(4)

    // fproduct
    val source = List("Cats", "is", "awesome")
    val product = Functor[List].fproduct(source)(_.length).toMap

    println(product) // Map("Cats" -> 4, "is" -> 2, "awesome" -> 7)
    println(product.getOrElse("Cats", 0))
    println(product.getOrElse("Dogs", 0))

    // Composing functors NOTE - Interesting type signature
    val listOptFunctor: Functor[({
      type λ[α] = List[Option[α]]
    })#λ] = Functor[List] compose Functor[Option]
    println(listOptFunctor.map(List(Some(1), None, Some(3)))(_ + 1)) // List(Some(2), None, Some(4))
  }
}
