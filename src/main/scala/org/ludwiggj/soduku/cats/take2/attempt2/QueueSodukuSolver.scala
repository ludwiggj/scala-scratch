package org.ludwiggj.soduku.cats.take2.attempt2

import cats.effect.IO
import cats.effect.std.Queue
import cats.implicits.{catsSyntaxParallelTraverse1, toTraverseOps}
import org.ludwiggj.soduku.cats.model.{Coord, Solver, Value}

object QueueSodukuSolver extends Solver[IO] {
  override def solve(givens: List[Value.Given]): IO[List[Value]] = {
    val coordQueueListIO: IO[List[(Coord, Queue[IO, Value])]] =
      Coord.allCoords.traverse(coord => Queue.unbounded[IO, Value].map(queue => (coord, queue)))
    for {
      coordQueueList <- coordQueueListIO
      allCells = coordQueueList.map { case (coord, queue) => new Cell(coord, queue) }
      givensMap = givens.map(g => g.coord -> g).toMap
      values <- allCells.parTraverse(cell => cell.solve(givensMap, allCells))
    } yield values
  }
}
