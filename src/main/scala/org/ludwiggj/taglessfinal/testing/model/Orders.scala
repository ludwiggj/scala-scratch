package org.ludwiggj.taglessfinal.testing.model

import org.ludwiggj.taglessfinal.testing.model.Model.{Order, UserId}

trait Orders[F[_]] {
  def ordersFor(userId: UserId): F[List[Order]]
}

object Orders {
  def apply[F[_]](implicit o: Orders[F]): Orders[F] = o
}
