package org.ludwiggj.soduku.cats.take2.attempt3

import cats.effect.IO
import cats.effect.std.Queue
import cats.implicits.{catsSyntaxParallelTraverse1, catsSyntaxParallelTraverse_, toTraverseOps}
import org.ludwiggj.soduku.cats.model.{Coord, Solver, Value, ValueOrdering}

object QueueSodukuSolver extends Solver[IO] {
  implicit val valueOrdering: ValueOrdering.type = ValueOrdering
  override def solve(givens: List[Value.Given]): IO[List[Value]] = {
    (for {
      givenCoords <- IO(givens.map(_.coord).toSet)
      missingCoords = Coord.allCoords.filterNot(givenCoords.contains)
      missingCells <- missingCoords.traverse(MissingCell.make)
      broadcast = broadcastToPeers(missingCells)(_)
      _ <- givens.parTraverse_(broadcast)
      missingValues <- missingCells.parTraverse(cell => cell.solve.flatTap(broadcast))
    } yield givens ++ missingValues).map(_.sorted)
  }

  def broadcastToPeers(cells: List[MissingCell])(update: Value): IO[Unit] =
    cells
      .filter(cell => cell.coord.isPeerOf(update.coord))
      .parTraverse_(cell => cell.updatesQueue.offer(update))
}
