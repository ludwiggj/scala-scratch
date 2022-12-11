package org.ludwiggj.soduku.cats.take1

import cats.effect.unsafe.implicits.global
import org.ludwiggj.soduku.Board.{Board, prettyString}
import org.ludwiggj.soduku.cats.model.Value.Given
import org.ludwiggj.soduku.cats.model.{Coord, Value}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class DeferredSodukuSolverTest extends AnyFlatSpec with Matchers {
  def toBoard(values: List[Value]): Board =
    values.map(_.value).toArray.grouped(9).toArray

  it should "solve a soduku problem" in {
    val givens:List[Given] = List(
      Given(Coord(row = 0, col = 0), 5),
      Given(Coord(row = 0, col = 1), 3),
      Given(Coord(row = 0, col = 4), 7),
      Given(Coord(row = 1, col = 0), 6),
      Given(Coord(row = 1, col = 3), 1),
      Given(Coord(row = 1, col = 4), 9),
      Given(Coord(row = 1, col = 5), 5),
      Given(Coord(row = 2, col = 1), 9),
      Given(Coord(row = 2, col = 2), 8),
      Given(Coord(row = 2, col = 7), 6),
      Given(Coord(row = 3, col = 0), 8),
      Given(Coord(row = 3, col = 4), 6),
      Given(Coord(row = 3, col = 8), 3),
      Given(Coord(row = 4, col = 0), 4),
      Given(Coord(row = 4, col = 3), 8),
      Given(Coord(row = 4, col = 5), 3),
      Given(Coord(row = 4, col = 8), 1),
      Given(Coord(row = 5, col = 0), 7),
      Given(Coord(row = 5, col = 4), 2),
      Given(Coord(row = 5, col = 8), 6),
      Given(Coord(row = 6, col = 1), 6),
      Given(Coord(row = 6, col = 6), 2),
      Given(Coord(row = 6, col = 7), 8),
      Given(Coord(row = 7, col = 3), 4),
      Given(Coord(row = 7, col = 4), 1),
      Given(Coord(row = 7, col = 5), 9),
      Given(Coord(row = 7, col = 8), 5),
      Given(Coord(row = 8, col = 4), 8),
      Given(Coord(row = 8, col = 7), 7),
      Given(Coord(row = 8, col = 8), 9)
    )

    val solution: List[Value] = CatsEffectDeferredRefRaceSolver.solve(givens).unsafeRunSync()
    solution.length shouldBe 81
    solution.map(_.value) should not contain 0

    val board: Board = solution.map(_.value).toArray.grouped(9).toArray
    println(prettyString(board))
  }

  it should "solve another soduku problem" in {
    val givens:List[Given] = List(
      Given(Coord(row = 0, col = 1), 6),
      Given(Coord(row = 0, col = 3), 3),
      Given(Coord(row = 0, col = 6), 8),
      Given(Coord(row = 0, col = 8), 4),
      Given(Coord(row = 1, col = 0), 5),
      Given(Coord(row = 1, col = 1), 3),
      Given(Coord(row = 1, col = 2), 7),
      Given(Coord(row = 1, col = 4), 9),
      Given(Coord(row = 2, col = 1), 4),
      Given(Coord(row = 2, col = 5), 6),
      Given(Coord(row = 2, col = 6), 3),
      Given(Coord(row = 2, col = 8), 7),
      Given(Coord(row = 3, col = 1), 9),
      Given(Coord(row = 3, col = 4), 5),
      Given(Coord(row = 3, col = 5), 1),
      Given(Coord(row = 3, col = 6), 2),
      Given(Coord(row = 3, col = 7), 3),
      Given(Coord(row = 3, col = 8), 8),
      Given(Coord(row = 5, col = 0), 7),
      Given(Coord(row = 5, col = 1), 1),
      Given(Coord(row = 5, col = 2), 3),
      Given(Coord(row = 5, col = 3), 6),
      Given(Coord(row = 5, col = 4), 2),
      Given(Coord(row = 5, col = 7), 4),
      Given(Coord(row = 6, col = 0), 3),
      Given(Coord(row = 6, col = 2), 6),
      Given(Coord(row = 6, col = 3), 4),
      Given(Coord(row = 6, col = 7), 1),
      Given(Coord(row = 7, col = 4), 6),
      Given(Coord(row = 7, col = 6), 5),
      Given(Coord(row = 7, col = 7), 2),
      Given(Coord(row = 7, col = 8), 3),
      Given(Coord(row = 8, col = 0), 1),
      Given(Coord(row = 8, col = 2), 2),
      Given(Coord(row = 8, col = 5), 9),
      Given(Coord(row = 8, col = 7), 8)
    )

    val solution: List[Value] = CatsEffectDeferredRefRaceSolver.solve(givens).unsafeRunSync()
    solution.length shouldBe 81
    solution.map(_.value) should not contain 0

    val board: Board = solution.map(_.value).toArray.grouped(9).toArray
    println(prettyString(board))
  }
}
