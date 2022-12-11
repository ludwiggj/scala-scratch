package org.ludwiggj.soduku

import org.ludwiggj.soduku.SodukuSolver.{findBox, problem, validate}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class SodukuSolverTest extends AnyFlatSpec with Matchers {

  it should "say it's valid if new value doesn't create duplicate in its row" in {
    validate(problem, col = 2, row = 0, value = 4) shouldBe true
  }

  it should "say it's invalid if new value creates duplicate in its row" in {
    validate(problem, col = 2, row = 0, value = 3) shouldBe false
  }

  it should "say it's valid if new value doesn't create duplicate in its column" in {
    validate(problem, col = 2, row = 0, value = 2) shouldBe true
  }

  it should "say it's invalid if new value creates duplicate in its column" in {
    validate(problem, col = 2, row = 0, value = 8) shouldBe false
  }

  it should "calculate 3 x 3 box" in {
    findBox(problem, col = 3, row = 0) shouldBe Array(0, 7, 0, 1, 9, 5, 0, 0, 0)
    findBox(problem, col = 4, row = 4) shouldBe Array(0, 6, 0, 8, 0, 3, 0, 2, 0)
    findBox(problem, col = 7, row = 8) shouldBe Array(2, 8, 0, 0, 0, 5, 0, 7, 9)
  }

  it should "say it's valid if new value doesn't create duplicate in 3x3 box" in {
    validate(problem, col = 2, row = 0, value = 4) shouldBe true
  }

  it should "say it's invalid if new value creates duplicate in its 3x3 box" in {
    validate(problem, col = 2, row = 0, value = 9) shouldBe false
  }
}
