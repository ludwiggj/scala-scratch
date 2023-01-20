package org.ludwiggj.taglessfinal.testing.wix.model

import org.ludwiggj.taglessfinal.testing.wix.model.Model.{UserId, UserProfile}

trait Users[F[_]] {
  def profileFor(userId: UserId): F[UserProfile]
}

object Users {
  def apply[F[_]](implicit u: Users[F]): Users[F] = u
}