package org.ludwiggj.soduku.cats.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class CoordTest extends AnyWordSpecLike with Matchers {
  val coord_R0C0: Coord = Coord(row = 0, col = 0)
  val coord_R0C1: Coord = Coord(row = 0, col = 1)
  val coord_R0C5: Coord = Coord(row = 0, col = 5)
  val coord_R1C0: Coord = Coord(row = 1, col = 0)
  val coord_R1C1: Coord = Coord(row = 1, col = 1)
  val coord_R2C0: Coord = Coord(row = 3, col = 0)
  val coord_R3C0: Coord = Coord(row = 3, col = 0)
  val coord_R4C4: Coord = Coord(row = 4, col = 4)
  val coord_R5C5: Coord = Coord(row = 5, col = 5)
  val coord_R9C0: Coord = Coord(row = 9, col = 0)

  "Coord.inSameRowAs" should {
    "return whether coords are in same row" in {
      coord_R0C0.inSameRowAs(coord_R0C0) shouldBe true
      coord_R0C0.inSameRowAs(coord_R0C1) shouldBe true
      coord_R0C0.inSameRowAs(coord_R1C0) shouldBe false
    }
  }

  "Coord.inSameColAs" should {
    "return whether coords are in same column" in {
      coord_R0C0.inSameColAs(coord_R0C0) shouldBe true
      coord_R0C1.inSameColAs(coord_R1C1) shouldBe true
      coord_R0C0.inSameColAs(coord_R1C1) shouldBe false
    }
  }

  "Coord.inSameBoxAs" should {
    "return whether coords are in same box" in {
      coord_R0C0.inSameBoxAs(coord_R0C0) shouldBe true
      coord_R0C0.inSameBoxAs(coord_R0C1) shouldBe true
      coord_R0C0.inSameBoxAs(coord_R3C0) shouldBe false
    }
  }

  "Coord.notThis" should {
    "return whether coords are the same" in {
      coord_R0C0.notThis(coord_R0C1) shouldBe true
      coord_R0C1.notThis(coord_R0C1) shouldBe false
    }
  }

  "Coord.isPeerOf" should {
    "return whether coords are peers" in {
      coord_R0C0.isPeerOf(coord_R0C5) shouldBe true // same row
      coord_R0C0.isPeerOf(coord_R0C1) shouldBe true // same row and box
      coord_R0C0.isPeerOf(coord_R9C0) shouldBe true // same column
      coord_R0C0.isPeerOf(coord_R2C0) shouldBe true // same column and box
      coord_R4C4.isPeerOf(coord_R5C5) shouldBe true // same box

      coord_R0C1.isPeerOf(coord_R0C1) shouldBe false // same coord
      coord_R0C1.isPeerOf(coord_R5C5) shouldBe false // different row, column and box
    }
  }
}
