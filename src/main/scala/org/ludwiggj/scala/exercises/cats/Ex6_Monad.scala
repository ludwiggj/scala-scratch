package org.ludwiggj.scala.exercises.cats

import cats.{Applicative, Monad}
import cats.data.OptionT
import cats.instances.future._
import cats.instances.list._
import scala.concurrent.Future

// (6) Monad - flatten, flatMap
object Ex6_Monad {
  def main(args: Array[String]): Unit = {
    println(Option(Option(1)).flatten) // Some(1)
    println(Monad[Option].flatten(Option(Option(1)))) // Some(1)
    println(Monad[Option].flatMap(Option(1))(x => Option(x + 1))) // Some(2)
    println(Monad[List].flatMap(List(1, 2, 3))(x => List(x, x)))  // List(1, 1, 2, 2, 3, 3)

    // We can use flatten to define flatMap: flatMap is just map followed by flatten.
    // Conversely, flatten is just flatMap using the identity function x => x (i.e. flatMap(_)(x => x)).

    // trait FlatMap[F[_]] extends Apply[F] {
    //  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
    //
    //  def flatten[A](ffa: F[F[A]]): F[A] =
    //    flatMap(ffa)(fa => fa)
    //
    //  def ...
    // }

    // To provide evidence that a type belongs in the Monad type class, cats' implementation requires us to provide an
    // implementation of pure (which can be reused from Applicative) and flatMap.

    // flatMap is often considered to be the core function of Monad, and cats follows this tradition by providing
    // implementations of flatten and map derived from flatMap and pure.
    //
    // implicit val listMonad = new Monad[List] {
    //   def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = fa.flatMap(f)
    //   def pure[A](a: A): List[A] = List(a)
    // }

    // Part of the reason for this is that name flatMap has special significance in scala, as for-comprehensions rely on
    // this method to chain together operations in a monadic context.

    // ifM Monad provides the ability to choose later operations in a sequence based on the results of earlier ones.
    // This is embodied in ifM, which lifts an if statement into the monadic context.

    // def ifM[B](fa: F[Boolean])(ifTrue: => F[B], ifFalse: => F[B]): F[B]
    println(Monad[Option].ifM(Option(true))(Option("truthy"), Option("falsy"))) // Some("truthy")
    println(Monad[List].ifM(List(true, false, true))(List(1, 2), List(3, 4)))   // List(1, 2, 3, 4, 1, 2)

    // Composition - Unlike Functors and Applicatives, you cannot derive a monad instance for a generic M[N[_]] where
    // both M[_] and N[_] have an instance of a monad.
    // However, it is common to want to compose the effects of both M[_] and N[_]. One way of expressing this is to
    // provide instructions on how to compose any outer monad (F in the following example) with a specific inner monad
    // (Option in the following example)

    // This is the monad transformer e.g.

    //    case class OptionT[F[_], A](value: F[Option[A]])
    //
    //    implicit def optionTMonad[F[_]](implicit F: Monad[F]) = {
    //      new Monad[OptionT[F, *]] {
    //        def pure[A](a: A): OptionT[F, A] = OptionT(F.pure(Some(a)))
    //        def flatMap[A, B](fa: OptionT[F, A])(f: A => OptionT[F, B]): OptionT[F, B] =
    //          OptionT {
    //            F.flatMap(fa.value) {
    //              case None => F.pure(None)
    //              case Some(a) => f(a).value
    //            }
    //          }
    //        def tailRecM[A, B](a: A)(f: A => OptionT[F, Either[A, B]]): OptionT[F, B] =
    //          defaultTailRecM(a)(f)
    //      }
    //    }

    println(OptionT[List, Int](List(Some(42)))) // OptionT(List(Some(42)))

    implicit def appList: Applicative[List] = Applicative[List]
    val yo: OptionT[List, Int] = OptionT.pure(42) // OptionT(List(Some(42)))
    val yoyo = OptionT.pure[List](42) // OptionT(List(Some(42)))

    println(yo)
    println(yoyo)

    import scala.concurrent.ExecutionContext.Implicits.global
    implicit def applicativeFuture: Applicative[Future] = Applicative[Future]

    val greet: OptionT[Future, String] = OptionT.pure("Hola!") // OptionT(Future(Success(Some(Hola!))))
    println(greet)
  }
}
