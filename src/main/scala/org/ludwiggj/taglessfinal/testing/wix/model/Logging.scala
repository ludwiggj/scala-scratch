package org.ludwiggj.taglessfinal.testing.wix.model

trait Logging[F[_]] {
  def error(e: Throwable): F[Unit]
}

object Logging {
  def apply[F[_]](implicit l: Logging[F]): Logging[F] = l
}