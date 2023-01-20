package org.ludwiggj.scala.exercises.cats

import scala.concurrent.Future
import cats.Semigroup
import cats.data.{ NonEmptyList, OneAnd, Validated, ValidatedNel }
import cats.implicits._

object Ex8_Traverse {

  def parseInt(s: String): Option[Int] = ???

  trait SecurityError
  trait Credentials

  def validateLogin(cred: Credentials): Either[SecurityError, Unit] = ???

  trait Profile
  trait User

  def userInfo(user: User): Future[Profile] = ???

  def profilesFor(users: List[User]): List[Future[Profile]] = users.map(userInfo)

  // trait Traverse[F[_]] {
  //  def traverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]]
  //}

  def parseIntEither(s: String): Either[NumberFormatException, Int] =
    Either.catchOnly[NumberFormatException](s.toInt)

  def parseIntValidated(s: String): ValidatedNel[NumberFormatException, Int] =
    Validated.catchOnly[NumberFormatException](s.toInt).toValidatedNel

  def main(args: Array[String]): Unit = {
    println(List("1", "2", "3").traverse(parseIntEither)) // Right(List(1, 2, 3))
    println(List("1", "abc", "def").traverse(parseIntEither)) // Left(java.lang.NumberFormatException: For input string: "abc")
    println(List("1", "2", "3").traverse(parseIntValidated)) // Valid(List(1, 2, 3))
    //  The behavior of traversal is closely tied with the Applicative behavior of the data type
    println(List("1", "abc", "def").traverse(parseIntValidated)) // Invalid(NonEmptyList(java.lang.NumberFormatException: For input string: "abc", java.lang.NumberFormatException: For input string: "def"))

    // Sequencing

    // trait Traverse[F[_]] {
    //  def traverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]]
    //}
    println(List(Option(1), Option(2), Option(3)).traverse(identity)) // Some(List(1,2,3))
    println(List(Option(1), None, Option(3)).traverse(identity))      // None

    // Traversing solely for the sake of the effect (ignoring any values that may be produced, Unit or otherwise) is
    // common, so Foldable (superclass of Traverse) provides traverse_ and sequence_ methods that do the same thing as
    // traverse and sequence but ignores any value produced along the way, returning Unit at the end.
    println(List(Option(1), Option(2), Option(3)).sequence_) // Some(())
    println(List(Option(1), None, Option(3)).sequence_)      // None
  }
}
