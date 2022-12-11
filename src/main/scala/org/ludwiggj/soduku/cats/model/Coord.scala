package org.ludwiggj.soduku.cats.model

case class Coord(row: Int, col: Int) {
  def inSameRowAs(that: Coord): Boolean = row == that.row
  def inSameColAs(that: Coord): Boolean = col == that.col
  def inSameBoxAs(that: Coord): Boolean = (row/3 == that.row/3) && (col/3 == that.col/3)
  def notThis(that: Coord) = this != that
  def isPeerOf(that: Coord): Boolean =
    (inSameRowAs(that) || inSameColAs(that) || inSameBoxAs(that)) && notThis(that)
}

object Coord {
  val rowIndices: List[Int] = (0 to 8).toList
  val colIndices: List[Int] = (0 to 8).toList

  val allCoords = for {
    row <- rowIndices
    col <- colIndices
  } yield Coord(row, col)
}
