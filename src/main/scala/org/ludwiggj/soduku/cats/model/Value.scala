package org.ludwiggj.soduku.cats.model

sealed trait Value {
  val coord: Coord
  val value: Int
}

object Value {
  case class Given(coord: Coord, value: Int) extends Value
}

// Hmm, this smells
sealed trait Candidate {
  val coord: Coord
}

object Candidate {
  class SingleCandidate private[Candidate](val coord: Coord, val value: Int) extends Value with Candidate {
    override def toString: String = s"Calc($coord,$value)"
  }

  class MultipleCandidate private[Candidate](val coord: Coord, val candidates: Set[Int]) extends Candidate {
    def refine(peerValue: Value): Candidate = {
      // Following works as option is iterable
      val newValues: Set[Int] = candidates -- Option.when(coord.isPeerOf(peerValue.coord))(peerValue.value)
      newValues.toList match {
        case Nil => throw new IllegalStateException()
        case singleCandidate :: Nil => new SingleCandidate(coord, singleCandidate)
        case multipleCandidates => new MultipleCandidate(coord, multipleCandidates.toSet)
      }
    }
  }

  def initial(coord: Coord): MultipleCandidate = new MultipleCandidate(coord, (1 to 9).toSet)
}
