package org.ludwiggj.taglessfinal.testing.take1

import cats.data.State
import org.ludwiggj.taglessfinal.testing.Logging
import org.ludwiggj.taglessfinal.testing.model.Model.{Order, UserId, UserProfile}
import org.ludwiggj.taglessfinal.testing.model.{Orders, Users}

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
  final val Empty: TestEnv = TestEnv(Map.empty, Map.empty, List.empty)

  type Test[A] = State[TestEnv, A]

  implicit val usersTest: Users[Test] = new Users[Test] {
    // Returns State[TestEnv, UserProfile]
    override def profileFor(userId: UserId): Test[UserProfile] =
      // Inspect a value from the input state, without modifying the state.
      State.inspect[TestEnv, UserProfile](testEnv => testEnv.profiles(userId))
  }

  implicit val ordersTest: Orders[Test] = new Orders[Test] {
    // Returns State[TestEnv, List[Order]]
    override def ordersFor(userId: UserId): Test[List[Order]] =
      // Inspect a value from the input state, without modifying the state.
      State.inspect(testEnv => testEnv.orders(userId))
  }

  implicit val loggingTest: Logging[Test] = new Logging[Test] {
    // modify the state, capturing the error
    override def error(e: Throwable): Test[Unit] =
      State.modify(testEnv => testEnv.logError(e))
  }
}