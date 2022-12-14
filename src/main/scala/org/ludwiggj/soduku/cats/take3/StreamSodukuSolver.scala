package org.ludwiggj.soduku.cats.take3

import cats.effect.{IO, Resource}
import cats.implicits.{catsSyntaxParallelTraverse1, toTraverseOps}
import fs2.concurrent.Topic
import fs2.Stream
import org.ludwiggj.soduku.cats.model.Value.Given
import org.ludwiggj.soduku.cats.model.{Candidate, Coord, Solver, Value, ValueOrdering}

object StreamSodukuSolver extends Solver[IO] {
  implicit val valueOrdering: ValueOrdering.type = ValueOrdering

  override def solve(givens: List[Value.Given]): IO[List[Value]] =
    valuesStream(givens).compile.toList.map(_.sorted)

  def missingValueStreamResource(
    updatesTopic: Topic[IO, Value]
  )(coord: Coord): Resource[IO, Stream[IO, Candidate.Single]] =
    updatesTopic
      .subscribeAwait(81)
      .map { updatesStream =>
        updatesStream
          .filter(_.coord.isPeerOf(coord))
          // def mapAccumulate[S, O2](init: S)(f: (S, O) => (S, O2)): Stream[F, (S, O2)]
          .mapAccumulate[Candidate, Candidate](Candidate.initial(coord)) {
            case (multiple: Candidate.Multiple, peerValue) =>
              val nextCandidate: Candidate = multiple.refine(peerValue)
              (nextCandidate, nextCandidate)
            case (single: Candidate.Single, _) => (single, single)
          }
          .collectFirst { case (_, single: Candidate.Single) => single }
      }

  def valuesStream(givens: List[Given]): Stream[IO, Value] =
    for {
      updatesTopic <- Stream.eval(Topic[IO, Value])
      givenCoords = givens.map(_.coord).toSet
      missingCoords = Coord.allCoords.filterNot(givenCoords.contains)
      givenValuesStream = Stream.emits(givens)
      // map      => List[Resource[IO, Stream[IO, Candidate.Single]]]
      // traverse => Resource[IO, List[Stream[IO, Candidate.Single]]]
      missingValueStreamsResource = missingCoords.traverse(missingValueStreamResource(updatesTopic)(_))
      missingValueStreams <- Stream.resource(missingValueStreamsResource)
      missingValueStream = missingValueStreams.reduce(_ merge _)
      valuesStream = givenValuesStream ++ missingValueStream
      publishedValuesStream = valuesStream.evalTap(updatesTopic.publish1)
      value <- publishedValuesStream
    } yield value
}
