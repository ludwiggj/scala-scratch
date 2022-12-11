package org.ludwiggj.soduku.cats.model

trait Solver[F[_]] {
  def solve(givens: List[Value.Given]): F[List[Value]]
}
