package org.ludwiggj.taglessfinal.testing.wix.model

import org.ludwiggj.taglessfinal.testing.wix.model.Model.{Order, UserId}

trait Orders[F[_]] {
  def ordersFor(userId: UserId): F[List[Order]]
}

object Orders {
  def apply[F[_]](implicit o: Orders[F]): Orders[F] = o
}
