package org.ludwiggj.soduku.cats.take2.attempt3

import cats.effect.IO
import cats.effect.std.Queue
import org.ludwiggj.soduku.cats.model.{Candidate, Coord, Value}

case class MissingCell(coord: Coord, updatesQueue: Queue[IO, Value]) {
  val solve: IO[Candidate.Single] = refineToSingleCandidate(Candidate.initial(coord))

  private def refineToSingleCandidate(candidate: Candidate.Multiple): IO[Candidate.Single] =
    for {
      peerValue <- updatesQueue.take
      singleCandidate <- candidate.refine(peerValue) match {
        case single: Candidate.Single => IO.pure(single)
        case multiple: Candidate.Multiple => refineToSingleCandidate(multiple)
      }
    } yield singleCandidate
}

object MissingCell {
  def make(coord: Coord): IO[MissingCell] =
    Queue.unbounded[IO, Value].map(queue => MissingCell(coord, queue))
}