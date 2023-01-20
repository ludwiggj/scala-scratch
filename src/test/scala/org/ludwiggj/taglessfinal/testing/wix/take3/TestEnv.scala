package org.ludwiggj.taglessfinal.testing.wix.take3

import cats.data.{EitherT, State, StateT}
import cats.syntax.either._
import org.ludwiggj.taglessfinal.testing.wix.model.Model.{Order, UserId, UserProfile}
import org.ludwiggj.taglessfinal.testing.wix.model.{Logging, Orders, Users}

case class UserNotFound(userId: UserId) extends RuntimeException(s"User with id $userId does not exist")
case class OrdersNotFound(userId: UserId) extends RuntimeException(s"There are no orders for user with id $userId")

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

  // In order solve this issue (ability to inspect environment on failure, see take2 for more details), we must
  // tweak the effect type. Instead of
  //   TestEnv => Either[Throwable, (TestEnv, A)]              // type Test[A] = StateT[EitherThrowableOr, TestEnv, A]
  // we want:
  //   TestEnv => (TestEnv, Either[Throwable, A])
  // We still preserve our ability to raise errors — but are now able to examine the TestEnv in both success and
  // failure cases. We can achieve this by turning our effects stack inside-out, using the EitherT monad transformer

  // final case class EitherT[F[_], A, B](value: F[Either[A, B]])
  //
  // EitherT is a transformer for Either, allowing the effect of an arbitrary type constructor F to be combined with the
  // fail-fast effect of Either.
  //
  // EitherT[F, A, B] wraps a value of type F[Either[A, B]].
  // An F[C] can be lifted into an EitherT[F, A, C] via EitherT.right.
  // An F[C] can be lifted into an EitherT[F, C, B] via EitherT.left.

  type EitherThrowableOr[A] = Either[Throwable, A]
  type StateReturning[A] = State[TestEnv, A]
  type Test[A] = EitherT[StateReturning, Throwable, A]

  implicit val usersTest: Users[Test] = new Users[Test] {
    // profileFor method returns:
    //    Test[UserProfile]
    // => EitherT[StateReturning, Throwable, UserProfile]

    // Given:
    //    final case class EitherT[F[_], A, B](value: F[Either[A, B]])
    // Then:
    //    EitherT[StateReturning, Throwable, UserProfile](value: StateReturning[Either[Throwable, UserProfile]])
    // => EitherT[StateReturning, Throwable, UserProfile](value: State[TestEnv, Either[Throwable, UserProfile]])
    override def profileFor(userId: UserId): Test[UserProfile] = {
      // Inspect a value from the input state, without modifying the state
      EitherT {
        State.inspect[TestEnv, EitherThrowableOr[UserProfile]] {
          testEnv =>
            testEnv.profiles.get(userId) match {
              case Some(profile) => profile.asRight
              case None => UserNotFound(userId).asLeft
            }
        }
      }
    }
  }

  implicit val ordersTest: Orders[Test] = new Orders[Test] {
    override def ordersFor(userId: UserId): Test[List[Order]] = {
      // Inspect a value from the input state, without modifying the state.
      EitherT {
        State.inspect[TestEnv, EitherThrowableOr[List[Order]]] {
          testEnv =>
            testEnv.orders.get(userId) match {
              case Some(orders) => orders.asRight
              case None => OrdersNotFound(userId).asLeft
            }
        }
      }

    // If we don't want to model failures, following works (liftF is an alias for right)
    // EitherT.liftF(State.inspect(_.userOrders(userId)))
    }
  }

  implicit val loggingTest: Logging[Test] = new Logging[Test] {
    // modify the state, capturing the error
    override def error(e: Throwable): Test[Unit] =
      EitherT {
        // State[TestEnv, Either[Throwable, Unit]]
        State.modify[TestEnv](testEnv => testEnv.logError(e)).map(_.asRight)
      }

      // Better version
      // EitherT(State(env => (env.logError(e), Right(()))))
  }
}
