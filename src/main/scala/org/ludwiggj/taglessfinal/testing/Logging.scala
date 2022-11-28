package org.ludwiggj.taglessfinal.testing

trait Logging[F[_]] {
  def error(e: Throwable): F[Unit]
}

object Logging {
  def apply[F[_]](implicit l: Logging[F]): Logging[F] = l
}