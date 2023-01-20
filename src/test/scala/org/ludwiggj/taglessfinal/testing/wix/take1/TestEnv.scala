package org.ludwiggj.taglessfinal.testing.wix.take1

import cats.data.State
import org.ludwiggj.taglessfinal.testing.wix.model.Model.{Order, UserId, UserProfile}
import org.ludwiggj.taglessfinal.testing.wix.model.{Logging, Orders, Users}

// Provides input to test (profiles & orders)
// Records output (loggedErrors)
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

  // State is a structure that provides a functional approach to handling application state.
  // State[S, A] is basically a function S => (S, A), where S is the type that represents your state and A is the result
  // the function produces. In addition to returning the result of type A, the function returns a new S value, which is
  // the updated state.
  type Test[A] = State[TestEnv, A] // TestEnv => (TestEnv, A)

  // Type class instances (or interpreters) for Users, Order, Logging
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