package org.ludwiggj.soduku

import org.ludwiggj.soduku.Board.{Board, prettyString}

// https://blog.rockthejvm.com/sudoku-backtracking/

object SodukuSolver {
  val problem: Board =
    Array(
      Array(5, 3, 0, 0, 7, 0, 0, 0, 0),
      Array(6, 0, 0, 1, 9, 5, 0, 0, 0),
      Array(0, 9, 8, 0, 0, 0, 0, 6, 0),
      Array(8, 0, 0, 0, 6, 0, 0, 0, 3),
      Array(4, 0, 0, 8, 0, 3, 0, 0, 1),
      Array(7, 0, 0, 0, 2, 0, 0, 0, 6),
      Array(0, 6, 0, 0, 0, 0, 2, 8, 0),
      Array(0, 0, 0, 4, 1, 9, 0, 0, 5),
      Array(0, 0, 0, 0, 8, 0, 0, 7, 9),
    )

  def findBox(soduku: Board, col: Int, row: Int): Array[Int] = {
    val startRowBlock: Int = row / 3
    val startColBlock: Int = col / 3

    (for {
      row <- startRowBlock * 3 until (startRowBlock + 1) * 3
      col <- startColBlock * 3 until (startColBlock + 1) * 3
    } yield soduku(row)(col)).toArray
  }

  def validate(soduku: Board, col: Int, row: Int, value: Int): Boolean = {
    def validate(existingNumbers: Array[Int]): Boolean = {
      !existingNumbers.contains(value)
    }

    validate(soduku(row)) &&
      validate(soduku.map(row => row(col))) &&
      validate(findBox(soduku, col, row))
  }

  def solve(soduku: Board, col: Int = 0, row: Int = 0): Unit = {
    def next(newSoduku:Board): Unit = {
      if (col == 8) {
        solve(newSoduku, col = 0, row + 1)
      } else {
        solve(newSoduku, col + 1, row)
      }
    }

    if (row == 9) {
      println(s"Solved!\n${prettyString(soduku)}")
    } else {
      if (soduku(row)(col) != 0) {
        next(soduku)
      } else {
        for (value <- 1 to 9) {
          if (validate(soduku, col, row, value)) {
            val newSoduku: Board = soduku.map(_.clone())
            newSoduku(row)(col) = value
            next(newSoduku)
          }
        }
      }
    }
  }

  def main(args: Array[String]): Unit = {
    println(prettyString(problem))
    solve(problem)
  }
}
