package org.ludwiggj.soduku.cats.take1

import cats.effect.IO
import cats.effect.kernel.{Deferred, Ref}
import org.ludwiggj.soduku.cats.model.{Candidate, Coord, Value}

trait Cell {
  def coord: Coord
  protected[this] def deferredValue: Deferred[IO, Value]
  // Deferred.get is semantically blocking - see https://typelevel.org/cats-effect/docs/thread-model for details
  def getValue: IO[Value] = deferredValue.get
  def deduceSingleCandidate(allCells: List[Cell]): IO[Value]
  def solve(givensMap: Map[Coord, Value.Given], allCells: List[Cell]): IO[Value] =
    (givensMap.get(coord) match {
      case Some(givenValue) => IO.pure(givenValue)
      case None => deduceSingleCandidate(allCells)
    }).flatTap(deferredValue.complete)
}

object Cell {
  def make(_coord: Coord): IO[Cell] =
    for {
      _deferredValue <- Deferred[IO, Value]
    } yield new Cell {
      override val coord: Coord = _coord

      override val deferredValue: Deferred[IO, Value] = _deferredValue

      override def deduceSingleCandidate(allCells: List[Cell]): IO[Candidate.Single] =
        for {
          refCandidate <- Ref.of[IO, Candidate](Candidate.initial(coord))
          peerCells = allCells.filter(cell => cell.coord.isPeerOf(coord))
          listOfSingleCandidateOrNever = peerCells.map(peerCell => // List[IO[Candidate.SingleCandidate]]
            refineToSingleCandidateOrNever(refCandidate, peerCell)
          )
          singleCandidate <- raceMany(listOfSingleCandidateOrNever)
        } yield singleCandidate

      private def raceMany[T](listOfIOs: List[IO[T]]): IO[T] =
        listOfIOs.reduce((a, b) => a.race(b).map(_.merge))

      private def refineToSingleCandidateOrNever(
        refCandidate: Ref[IO, Candidate],
        peerCell: Cell
      ): IO[Candidate.Single] =
        for {
          peerValue <- peerCell.getValue
          singleCandidate <- refCandidate.modify {
            case multiple: Candidate.Multiple =>
              multiple.refine(peerValue) match {
                case single: Candidate.Single => (single, IO.pure(single))
                case multiple: Candidate.Multiple => (multiple, IO.never)
              }
            case alreadySingle: Candidate.Single => (alreadySingle, IO.never)

          }.flatten
        } yield singleCandidate
    }
}
