package org.ludwiggj.scala.exercises.cats

import cats.instances.int._
import cats.instances.list._
import cats.instances.option._
import cats.instances.string._
import cats.kernel.Monoid
import cats.syntax.either._
import cats.{Foldable, Later, MonoidK, Now}

// (7) Foldable
object Ex7_Foldable {

  def main(args: Array[String]): Unit = {
    // foldLeft is an eager left-associative fold on F using the given function.

    // foldRight is a lazy right-associative fold on F using the given function
    // The function has the signature (A, Eval[B]) => Eval[B] to support laziness in a stack-safe way.
    val lazyResult = Foldable[List].foldRight(List(1, 2, 3), Now(0))((x, rest) => Later(x + rest.value))
    println(lazyResult.value) // 6

    // fold, also called combineAll, combines every value in the foldable using the given Monoid instance.
    println(Foldable[List].fold(List("a", "b", "c"))) // "abc"
    // println(Foldable[List].fold(List(Some("a"), Some("b"), Some("c")))) // this doesn't compile - see later

    // foldMap is similar to fold but maps every A value into B and then combines them using the given Monoid[B] instance.
    println(Foldable[List].foldMap(List("a", "b", "c"))(_.length)) // 3

    // foldK
    // def foldK[G[_], A](fga: F[G[A]])(implicit G: MonoidK[G]): G[A]

    // foldK is similar to fold but combines every value in the foldable using the given MonoidK[G] instance instead of
    // Monoid[G].

    // MonoidK is a universal monoid which operates on kinds.
    // This type class is useful when its type parameter F[_] has a structure that can be combined for any particular
    // type, and which also has an "empty" representation. Thus, MonoidK is like a Monoid for kinds (i.e. parametrized
    // types).

    // A MonoidK[F] can produce a Monoid[F[A]] for any type A.
    // Here's how to distinguish Monoid and MonoidK:

    // Monoid[A] allows A values to be combined, and also means there is an "empty" A value that functions as an
    // identity.

    // MonoidK[F] allows two F[A] values to be combined, for any A. It also means that for any A, there is an "empty"
    // F[A] value. The combination operation and empty value just depend on the structure of F, but not on the structure of A.

    println(Foldable[List].foldK(List(List(1, 2), List(3, 4, 5)))) // List(1, 2, 3, 4, 5)

    println(Foldable[List].foldK(List(None, Option("two"), Option("three")))) // Some("two") - first non-empty option
    println(Foldable[List].foldK(List(Option("one"), Option("two"), Option("three")))) // Some("one") - first non-empty option

    println(s"here: ${Monoid[String].combine("1", "2")}") // "12"
    println(Monoid[Option[String]].combine(Some("1"), Some("2"))) // Some("12")
    println(Monoid[Option[String]].combine(Some("1"), None)) // Some("1")
    println(Monoid[Option[String]].combine(None, Some("2"))) // Some("2")
    println(Monoid[Option[String]].combine(None, None)) // None

    // MonoidK ignores structure of A i.e. in this case, what's in the option
    println(s"there: ${MonoidK[Option].combineK(Some("1"), Some("2"))}") // Some("1")
    println(MonoidK[Option].combineK(Some("1"), None)) // Some("1")
    println(MonoidK[Option].combineK(None, Some("2"))) // Some("2")
    println(MonoidK[Option].combineK(None, None)) // None

    // find searches for the first element matching the predicate, if one exists.
    println(Foldable[List].find(List(1, 2, 3))(_ > 2)) // Some(3)

    // exists checks whether at least one element satisfies the predicate.
    println(Foldable[List].exists(List(1, 2, 3))(_ > 2)) // true

    // forall checks whether all elements satisfy the predicate
    println(Foldable[List].forall(List(1, 2, 3))(_ <= 3)) // true

    // toList Convert F[A] to List[A].
    println(Foldable[Option].toList(Option(123))) // List(123)

    // filter_, Convert F[A] to List[A] only including the elements that match a predicate.
    println(Foldable[List].filter_(List(1, 2, 3))(_ < 3)) // List(1, 2)
    println(Foldable[Option].filter_(Option(42))(_ != 42)) // List()

    // traverse_
    // def traverse_[G[_], A, B](fa: F[A])(f: A => G[B])(implicit G: Applicative[G]): G[Unit]

    // traverse the foldable mapping A values to G[B], and combining them using Applicative[G] and discarding the results.
    // This method is primarily useful when G[_] represents an action or effect, and the specific B aspect of G[B] is
    // not otherwise needed. The B will be discarded and Unit returned instead.

    def parseInt(s: String): Option[Int] =
      Either.catchOnly[NumberFormatException](s.toInt).toOption

    println(Foldable[List].traverse_(List("1", "2", "3"))(parseInt)) // Some(())

    // compose
    // We can compose Foldable[F[_]] and Foldable[G[_]] instances to obtain Foldable[F[G]].
    val FoldableListOption: Foldable[({
      type λ[α] = List[Option[α]]
    })#λ] = Foldable[List].compose[Option]

    // def fold[A](fa: F[A])(implicit A: Monoid[A]): A
    println(FoldableListOption.fold(List(Option(1), Option(2), Option(3), Option(4)))) // 10 Int - still returns inner type
    println(FoldableListOption.fold(List(Option("1"), Option("2"), None, Option("3")))) // "123" String

    // also: isEmpty, dropWhile, takeWhile
  }
}

