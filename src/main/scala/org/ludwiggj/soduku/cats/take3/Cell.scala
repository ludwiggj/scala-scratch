package org.ludwiggj.soduku.cats.take3

import cats.effect.IO
import cats.effect.std.Queue
import cats.effect.unsafe.implicits.global
import cats.implicits.toTraverseOps
import org.ludwiggj.soduku.cats.model.{Candidate, Coord, Value}

trait Cell {
  def coord: Coord

  def deduceSingleCandidate(): IO[Value]

  def solve(givensMap: Map[Coord, Value.Given], allCells: List[Cell]): IO[Value] =
    (givensMap.get(coord) match {
      case Some(givenValue) => IO.pure(givenValue)
      case None => deduceSingleCandidate()
    }).flatTap(value => IO.println(s"Solved $coord with value $value") *> IO(value)).flatTap(value => broadcast(value, peers(allCells)))

  val updatesQueue: Queue[IO, Value] = Queue.unbounded[IO, Value].unsafeRunSync()

  def peers(allCells: List[Cell]): List[Cell] = allCells.filter(_.coord.isPeerOf(coord))

  def broadcast(value: Value, peers: List[Cell]): IO[Unit]

  override def toString: String = coord.toString
}

object Cell {
  def make(_coord: Coord): IO[Cell] = IO(new Cell {
    override val coord: Coord = _coord

    override def deduceSingleCandidate(): IO[Candidate.Single] = {
      refineToSingleCandidate(Candidate.initial(coord))
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

    override def broadcast(value: Value, peers: List[Cell]): IO[Unit] = {
      for {
        _ <- IO.println(s"Broadcasting $value to ${peers.mkString("[", ", ", "]")}")
        _ <- peers.map(_.updatesQueue).traverse(_.offer(value))
      } yield ()
    }
  })
}
