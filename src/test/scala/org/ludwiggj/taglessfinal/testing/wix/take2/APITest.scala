package org.ludwiggj.taglessfinal.testing.wix.take2

import cats.implicits.catsSyntaxEitherId
import org.ludwiggj.taglessfinal.testing.wix.matchers.CustomMatchers._
import TestEnv.EitherThrowableOr
import org.ludwiggj.taglessfinal.testing.wix.model.Model
import org.ludwiggj.taglessfinal.testing.wix.model.Model.{Order, OrderId, UserId, UserProfile}
import org.ludwiggj.taglessfinal.testing.wix.API
import org.scalatest.EitherValues._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

// https://medium.com/wix-engineering/functional-testing-with-tagless-final-50eeacf5df6
class APITest extends AnyFlatSpec with Matchers {

  it should "fetch user name and orders by id" in {
    val userId: UserId = UserId("user-1234")

    val testEnv: TestEnv = TestEnv.Empty
      .withProfile(UserProfile(userId, "John Doe"))
      .withOrder(Order(userId, OrderId("order-1")))
      .withOrder(Order(userId, OrderId("order-2")))

    // Cats can now derive a type-class instance for MonadError[Test, Throwable]
    val result: EitherThrowableOr[Model.UserInformation] = API.fetchUserInformation[TestEnv.Test](userId).runA(testEnv)

    result.value should (haveUserName("John Doe") and haveOrders(OrderId("order-1"), OrderId("order-2")))
  }

  // This test verifies the error path
  // But we are unable to examine our TestEnv in case of failures
  // So we can’t check that our error was in fact logged correctly.

  // We can understand why that’s the case when we expand the definition of our Test effect:

  // Test[A]                                    -->          // type Test[A] = StateT[EitherThrowableOr, TestEnv, A]
  // StateT[EitherThrowableOr, TestEnv, A]      -->          // type EitherThrowableOr[A] = Either[Throwable, A]
  // StateT[Either[Throwable, A], TestEnv, A]   -->
  // TestEnv => Either[Throwable, (TestEnv, A)]

  // As we can see, when we raise an error, we will get it back in the left part of the Either,
  // but we lose the resulting environment. We can only check what happened with our environment
  // in the right part of the Either — namely, in the success case.

  // See take3 for how to deal with this
  it should "log an error if user does not exist" in {
    val userId: UserId = UserId("user-1234")
    val testEnv: TestEnv = TestEnv.Empty

    val result: EitherThrowableOr[Model.UserInformation] = API.fetchUserInformation[TestEnv.Test](userId).runA(testEnv)
    result.left.e shouldBe UserNotFound(userId).asLeft
  }
}
