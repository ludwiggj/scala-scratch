package org.ludwiggj.soduku.cats.take2.attempt2

import cats.effect.IO
import cats.effect.std.Queue
import cats.implicits.toTraverseOps
import org.ludwiggj.soduku.cats.model.{Candidate, Coord, Value}

class Cell(val coord: Coord, val updatesQueue: Queue[IO, Value]) {
  def solve(givensMap: Map[Coord, Value.Given], allCells: List[Cell]): IO[Value] =
    (givensMap.get(coord) match {
      case Some(givenValue) => IO.pure(givenValue)
      case None => refineToSingleCandidate(Candidate.initial(coord))
    }).flatTap {
      value => IO.println(s"Solved $coord with value $value") *> IO(value)
    }.flatTap {
      value => broadcast(value, allCells.filter(_.coord.isPeerOf(coord)))
    }

  private def refineToSingleCandidate(candidate: Candidate.Multiple): IO[Candidate.Single] =
    for {
      _ <- IO.println(s"Candidate $candidate ready for updates")
      value <- updatesQueue.take
      _ <- IO.println(s"Candidate $candidate received $value")
      candidate <- candidate.refine(value) match {
        case single: Candidate.Single => IO.pure(single)
        case multiple: Candidate.Multiple => refineToSingleCandidate(multiple)
      }
    } yield candidate

  private def broadcast(value: Value, peers: List[Cell]): IO[Unit] = {
    peers.map(_.updatesQueue).traverse(_.offer(value)) *>
      IO.unit
  }

  override def toString: String = coord.toString
}
