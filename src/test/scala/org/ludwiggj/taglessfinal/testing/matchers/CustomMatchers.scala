package org.ludwiggj.taglessfinal.testing.matchers

import org.ludwiggj.taglessfinal.testing.model.Model.{OrderId, UserInformation}
import org.ludwiggj.taglessfinal.testing.take2.UserNotFound
import org.ludwiggj.taglessfinal.testing.take3.TestEnv
import org.scalatest.matchers.{MatchResult, Matcher}

trait CustomMatchers {
  class UserNameMatcher(expectedName: String) extends Matcher[UserInformation] {
    override def apply(left: UserInformation): MatchResult = {
      val result = left.userName == expectedName
      MatchResult(
        result,
        s"User name ${left.userName} did not match expected name $expectedName",
        s"User name was $expectedName, as expected"
      )
    }
  }

  def haveUserName(userName: String): UserNameMatcher = new UserNameMatcher(userName)

  class OrderIdMatcher(orderIds: List[OrderId]) extends Matcher[UserInformation] {
    override def apply(left: UserInformation): MatchResult = {
      val actualOrderIds: List[OrderId] = left.orders.map(_.orderId)

      val invalidOrderIds: List[OrderId] = {
          orderIds.foldLeft(List.empty[OrderId]) {
          case (invalidList, orderId) => if (actualOrderIds.contains(orderId)) invalidList else invalidList :+ orderId
        }
      }

      MatchResult(
        invalidOrderIds.isEmpty,
        s"OrderIds $actualOrderIds did not contain expected order ids $invalidOrderIds",
        s"OrderIds $actualOrderIds contained expected order ids $orderIds"
      )
    }
  }

  def haveOrders(orderIds: OrderId*): OrderIdMatcher = new OrderIdMatcher(orderIds = orderIds.toList)

  class LoggedErrorMatcher(error: Throwable) extends Matcher[TestEnv] {
    override def apply(left: TestEnv): MatchResult = {
      val actualLoggedErrors: List[String] = left.loggedErrors.map(_.getMessage)

      MatchResult(
        actualLoggedErrors.contains(error.getMessage),
        s"Logged errors $actualLoggedErrors did not contain expected error ${error.getMessage}",
        s"Logged errors $actualLoggedErrors contains expected error ${error.getMessage}"
      )
    }
  }

  def containLoggedError(error: Throwable): LoggedErrorMatcher = new LoggedErrorMatcher(error)
}

object CustomMatchers extends CustomMatchers