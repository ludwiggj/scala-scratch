package org.ludwiggj.taglessfinal.testing.wix

import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.syntax.applicativeError._
import cats.MonadError
import org.ludwiggj.taglessfinal.testing.wix.model.Model._
import org.ludwiggj.taglessfinal.testing.wix.model._

object API {
  type MonadThrowable[F[_]] = MonadError[F, Throwable]

  def fetchUserInformation[F[_] : MonadThrowable : Users : Orders : Logging](userId: UserId): F[UserInformation] = {
    val result: F[UserInformation] = for {
      profile <- Users[F].profileFor(userId)
      orders <- Orders[F].ordersFor(userId)
    } yield UserInformation.from(profile, orders)

    result.onError {
      case e => Logging[F].error(e)
    }
  }
}
