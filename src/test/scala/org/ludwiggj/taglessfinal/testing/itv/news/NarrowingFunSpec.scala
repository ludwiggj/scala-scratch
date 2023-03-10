
package org.ludwiggj.taglessfinal.testing.itv.news

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.implicits.{catsSyntaxApplicativeError, catsSyntaxApplicativeErrorId}
import cats.syntax.either._
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class NarrowingFunSpec extends AnyWordSpecLike with Matchers {
  val five: Either[RuntimeException, Int] = 5.asRight

  "IO" should {
    val runtimeException: RuntimeException = new RuntimeException("not a number")

    "represent an error as an exception" in {
      // throws: java.lang.RuntimeException: not a number
      the [RuntimeException] thrownBy {
        IO.raiseError[Int](runtimeException).unsafeRunSync()
      } should have message "not a number"
    }

    "return a right when narrowing a valid IO " in {
      // Right(5)
      IO(5).attemptNarrow[RuntimeException].unsafeRunSync() shouldBe five
    }

    "return the exception as a left on a successful narrow of an IO error" in {
      // Left(java.lang.RuntimeException: not a number)
      IO.raiseError[Int](runtimeException).attemptNarrow[RuntimeException].unsafeRunSync() shouldBe runtimeException.asLeft
    }

    "return the exception unchanged on an unsuccessful narrow of an IO error" in {
      // throws: java.lang.RuntimeException: not a number
      the [Exception] thrownBy {
        IO.raiseError[Int](new Exception("not a number")).attemptNarrow[RuntimeException].unsafeRunSync()
      } should have message "not a number"
    }
  }

  "Either" should {
    val runtimeException: Either[RuntimeException, Int] = new RuntimeException("not a number").asLeft[Int]

    "represent an error (exception) as a left of left" in {
      // Left(Left(java.lang.RuntimeException: not a number))
      runtimeException.raiseError shouldBe runtimeException.asLeft
    }

    "represent an error (value) as a right of left" in {
      // Left(Right(5))
      five.raiseError shouldBe five.asLeft
    }

    "return a right of right when narrowing a right" in {
      // Right(Right(5))
      val narrowed: Either[RuntimeException, Either[RuntimeException, Int]] = five.attemptNarrow[RuntimeException]
      narrowed shouldBe five.asRight
    }

    "return a right of left on a successful narrow of a left" in {
      // Right(Left(java.lang.RuntimeException: not a number))
      val narrowed: Either[RuntimeException, Either[RuntimeException, Int]] = runtimeException.attemptNarrow[RuntimeException]
      narrowed shouldBe runtimeException.asRight[RuntimeException]
    }

    "return the left unchanged on an unsuccessful narrow of a left" in {
      // Left(java.lang.Exception: not a number)
      val exception: Either[Exception, Int] = new Exception("not a number").asLeft[Int]
      val narrowed: Either[Exception, Either[RuntimeException, Int]] = exception.attemptNarrow[RuntimeException]
      narrowed shouldBe exception // could not be narrowed, returned unchanged
    }
  }
}
