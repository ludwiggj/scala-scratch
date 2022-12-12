package org.ludwiggj.soduku.cats.take2.attempt

import cats.effect.IO
import cats.implicits.{catsSyntaxParallelTraverse1, toTraverseOps}
import org.ludwiggj.soduku.cats.model.{Coord, Solver, Value}

object QueueSodukuSolver extends Solver[IO] {
  override def solve(givens: List[Value.Given]): IO[List[Value]] =
    for {
      allCells <- Coord.allCoords.traverse(Cell.make)
      givensMap = givens.map(g => g.coord -> g).toMap
      values <- allCells.parTraverse(cell => cell.solve(givensMap, allCells))
    } yield values
}
