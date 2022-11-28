package org.ludwiggj.taglessfinal.testing

import cats.data.State
import org.ludwiggj.taglessfinal.testing.model.Model.{Order, UserId, UserProfile}
import org.ludwiggj.taglessfinal.testing.model.Users

// https://medium.com/wix-engineering/functional-testing-with-tagless-final-50eeacf5df6
case class TestEnv(
  profiles: Map[UserId, UserProfile],
  orders: Map[UserId, List[Order]],
  loggedErrors: List[Throwable]
) {
  def withProfile(profile: UserProfile): TestEnv =
    copy(profiles = profiles + (profile.userId -> profile))

  def withOrder(order: Order): TestEnv =
    copy(orders = orders + (order.userId -> (order :: userOrders(order.userId))))

  def userOrders(userId: UserId): List[Order] =
    orders.getOrElse(userId, List.empty)

  def logError(e: Throwable): TestEnv =
    copy(loggedErrors = e :: loggedErrors)
}

object TestEnv {
  final val Empty = TestEnv(Map.empty, Map.empty, List.empty)

  type Test[A] = State[TestEnv, A]

  implicit val usersTest: Users[Test] = new Users[Test] {
    override def profileFor(userId: UserId): Test[UserProfile] = { // Returns State[TestEnv, UserProfile]
      // Inspect a value from the input state, without modifying the state.
      State.inspect(testEnv => testEnv.profiles(userId))
    }
  }
}