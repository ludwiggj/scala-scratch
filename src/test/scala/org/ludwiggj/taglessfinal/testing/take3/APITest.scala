package org.ludwiggj.taglessfinal.testing.take3

import cats.implicits.catsSyntaxEitherId
import org.ludwiggj.taglessfinal.testing.API
import org.ludwiggj.taglessfinal.testing.matchers.CustomMatchers._
import org.ludwiggj.taglessfinal.testing.model.Model
import org.ludwiggj.taglessfinal.testing.model.Model.{Order, OrderId, UserId, UserProfile}
import TestEnv.EitherThrowableOr
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
    val result: Either[Throwable, Model.UserInformation] =
      API.fetchUserInformation[TestEnv.Test](userId).value.runA(testEnv).value

    result.getOrElse(fail(s"result [$result] is a left")) should
      (haveUserName("John Doe") and haveOrders(OrderId("order-1"), OrderId("order-2")))
  }

  it should "log an error if user does not exist" in {
    val userId: UserId = UserId("user-1234")
    val testEnv: TestEnv = TestEnv.Empty

    val (state, result): (TestEnv, Either[Throwable, Model.UserInformation]) =
      API.fetchUserInformation[TestEnv.Test](userId).value.run(testEnv).value

    result.left.e shouldBe UserNotFound(userId).asLeft
    state.loggedErrors.size shouldBe 1

    state should containLoggedError(UserNotFound(userId))
  }
}
