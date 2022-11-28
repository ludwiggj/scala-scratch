package org.ludwiggj.taglessfinal.testing

import cats.implicits._
import cats.MonadError
import org.ludwiggj.taglessfinal.testing.model.Model.{UserId, UserInformation}
import org.ludwiggj.taglessfinal.testing.model.{Orders, Users}


object API {
  type MonadThrowable[F[_]] = MonadError[F, Throwable]

  def fetchUserInformation[F[_]: MonadThrowable: Users: Orders: Logging](userId: UserId): F[UserInformation] = {
    val result = for {
      profile <- Users[F].profileFor(userId)
      orders <- Orders[F].ordersFor(userId)
    } yield UserInformation.from(profile, orders)

    result.onError {
      case e => Logging[F].error(e)
    }
  }
}
