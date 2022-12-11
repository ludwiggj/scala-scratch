package org.ludwiggj.taglessfinal.testing.take2

import cats.data.StateT
import cats.implicits._
import org.ludwiggj.taglessfinal.testing.Logging
import org.ludwiggj.taglessfinal.testing.model.Model.{Order, UserId, UserProfile}
import org.ludwiggj.taglessfinal.testing.model.{Orders, Users}

case class UserNotFound(userId: UserId) extends RuntimeException(s"User with id $userId does not exist")

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

  // Cats library provides us with a Monad instance for State out of the box, however we are still missing the
  // instance for MonadError[Test, Throwable]. Our chosen effect type is not suitable for dealing with errors,
  // so we need to tweak it a bit. State[S, A] is actually a type alias for StateT[Eval, S, A]. The most basic
  // wrapper for dealing with errors is Either. So we can redefine our test effect like so:

  type EitherThrowableOr[A] = Either[Throwable, A]
  type Test[A] = StateT[EitherThrowableOr, TestEnv, A]


  implicit val usersTest: Users[Test] = new Users[Test] {
    // Returns StateT[EitherThrowableOr, TestEnv, UserProfile]
    override def profileFor(userId: UserId): Test[UserProfile] =
      // Inspect a value from the input state, without modifying the state - now based on StateT
      // StateT.inspect[EitherThrowableOr, TestEnv, UserProfile](testEnv => testEnv.profiles(userId))

      // Refine to handle the case where a user for requested ID is missing
      StateT.inspectF[EitherThrowableOr, TestEnv, UserProfile] {
        testEnv =>
          testEnv.profiles.get(userId) match {
            case Some(profile) => Right(profile)
            case None => Left(UserNotFound(userId))
          }
      }
  }

  implicit val ordersTest: Orders[Test] = new Orders[Test] {
    // Returns StateT[EitherThrowableOr, TestEnv, List[Order]]
    override def ordersFor(userId: UserId): Test[List[Order]] =
      // Inspect a value from the input state, without modifying the state.
      StateT.inspect(testEnv => testEnv.orders(userId))
  }

  implicit val loggingTest: Logging[Test] = new Logging[Test] {
    // modify the state, capturing the error
    override def error(e: Throwable): Test[Unit] =
      StateT.modify(testEnv => testEnv.logError(e))
  }
}
