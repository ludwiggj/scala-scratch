package org.ludwiggj.scala.exercises.cats

import cats.Apply

// (4) apply - ap
object Ex4_Apply {
  def main(args: Array[String]): Unit = {
    // def ap[A, B](ff: F[A => B])(fa: F[A]): F[B]
    val plusOne: Int => Int = (x: Int) => x + 1

    println(Apply[List].ap(List(plusOne))(List(1, 2, 3))) // List(2, 3, 4)

    // Combining apply
    val listOptApply = Apply[List] compose Apply[Option]
    println(listOptApply.ap(List(Some(plusOne)))(List(Some(1), None, Some(3)))) // List(Some(2), None, Some(4))

    println(Apply[Option].ap(None)(Some(1))) // None

    // Explained by inspecting:

    // implicit val optionApply: Apply[Option] = new Apply[Option] {
    //  def ap[A, B](f: Option[A => B])(fa: Option[A]): Option[B] =
    //    fa.flatMap(a => f.map(ff => ff(a)))
    //
    //  def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa map f
    //
    //  def product[A, B](fa: Option[A], fb: Option[B]): Option[(A, B)] =
    //    fa.flatMap(a => fb.map(b => (a, b)))
    // }

    // product

    // ap also enables product, as follows; but note, in cats course, product is implemented on Semigroupal, from which
    // Apply inherits the method

    // Definition of product in Cats is:

    // def product[A, B](fa: F[A], fb: F[B]): F[(A, B)] =
    //    ap(map(fa)(a => (b: B) => (a, b)))(fb): F[(A, B)]
    //                                      F[B]

    // Remembering definition of ap:
    //   def ap[A, B](f: F[A => B])(fa: F[A]): F[B]

    // By comparing call to ap in product to ap's definition:
    //   A equiv B
    //   B equiv (A, B)

    // Substituting into ap definition:
    //   def ap[B, (A, B))](f: F[B => (A, B))])(fa: F[B]): F[(A, B)]

    // Comparing this type definition to call to ap in product implementation:

    //  ap(map(fa)(a => (b: B) => (a, b)))(fb): F[(A, B)]
    //     |--- F[B => (A, B))] --------| F[B]
    // So we expect
    //   map(fa)(a => (b: B) => (a, b)): F[B => (A, B))]
    // and remembering:
    //   def map[A, B](fa: F[A])(f: A => B): F[B] // from functor
    // we have:
    //   map(fa)(a => (b: B) => (a, b)): F[B]
    //      F[A] A => B
    // where
    //   B equiv B => (A, B)
    // So map's type signature is actually:
    //   map(fa)(a => (b: B) => (a, b)): F[B => (A, B)]
    //      F[A]|-- A => B => (A, B)--|
    // So the types line up

    // The product method (def product[A, B](fa: F[A], fb: F[B]): F[(A, B)]) enables:

    // def ap2[A, B, Z](ff: F[(A, B) => Z])(fa: F[A], fb: F[B]): F[Z] =
    //    map(product(fa, product(fb, ff))) { case (a, (b, f)) => f(a, b) }

    val addArity2: (Int, Int) => Int = (a: Int, b: Int) => a + b

    // Here addArity2 is placed in context of F
    println(Apply[Option].ap2(Some(addArity2))(Some(1), Some(2))) // Some(3)
    println(Apply[Option].ap2(Some(addArity2))(Some(1), None))    // None

    // Also enables mapN
    // def map2[A, B, Z](fa: F[A], fb: F[B])(f: (A, B) => Z): F[Z]

    // Here addArity2 is not placed in context of F
    println(Apply[Option].map2(Some(1), Some(2))(addArity2)) // Some(3)

    // And tupleN
    // def tuple2[A, B](f1: F[A], f2: F[B]): F[(A, B)]
    println(Apply[Option].tuple3(Some(1), Some(2), Some(3))) // Some(1, 2, 3)

    // syntax to make things easier
    import cats.implicits._
    val option2: (Option[Int], Option[Int]) = (Option(1), Option(2))
    val option3: (Option[Int], Option[Int], Option[Int]) = (option2._1, option2._2, Option.empty[Int]) // (Option(1), Option(2), None)

    val addArity3: (Int, Int, Int) => Int = (a: Int, b: Int, c: Int) => a + b + c

    println(option2 mapN addArity2) // Some(3)
    println(option3 mapN addArity3) // None

    println(option2 apWith Some(addArity2)) // Some(3)
    println(option3 apWith Some(addArity3)) // None

    println(option2.tupled) // Some(1, 2)
    println(option3.tupled) // None
  }
}
