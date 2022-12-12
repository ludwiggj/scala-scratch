package org.ludwiggj.soduku.cats.model

sealed trait Value {
  val coord: Coord
  val value: Int
}

object ValueOrdering extends Ordering[Value] {
  def compare(a: Value, b:Value): Int = {
    CoordOrdering.compare(a.coord, b.coord)
  }
}

object Value {
  case class Given(coord: Coord, value: Int) extends Value
}

// Hmm, this smells
sealed trait Candidate {
  val coord: Coord
}

object Candidate {
  class Single private[Candidate](val coord: Coord, val value: Int) extends Value with Candidate {
    override def toString: String = s"Single $coord Value $value)"
  }

  class Multiple private[Candidate](val coord: Coord, val candidates: Set[Int]) extends Candidate {
    def refine(peerValue: Value): Candidate = {
      // Following works as option is iterable
      val newValues: Set[Int] = candidates -- Option.when(coord.isPeerOf(peerValue.coord))(peerValue.value)
      newValues.toList match {
        case Nil => throw new IllegalStateException()
        case singleCandidate :: Nil => new Single(coord, singleCandidate)
        case multipleCandidates => new Multiple(coord, multipleCandidates.toSet)
      }
    }

    override def toString: String = s"Multiple $coord Possibles $candidates"
  }

  def initial(coord: Coord): Multiple = new Multiple(coord, (1 to 9).toSet)
}
