package org.ludwiggj.taglessfinal.testing.wix.take1

import org.ludwiggj.taglessfinal.testing.wix.model.Model.{Order, OrderId, UserId, UserProfile}
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

    // could not find implicit value for evidence parameter of type
    // org.ludwiggj.taglessfinal.testing.API.MonadThrowable[org.ludwiggj.taglessfinal.testing.take1.TestEnv.Test]

    // See take2 for the fix
    // val result = API.fetchUserInformation[TestEnv.Test](userId).runA(testEnv)

    // result.value should (haveUserName("John Doe") and haveOrders(OrderId("order-1"), OrderId("order-2")))
  }
}
