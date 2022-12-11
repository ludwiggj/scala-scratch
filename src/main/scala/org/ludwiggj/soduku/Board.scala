package org.ludwiggj.soduku

object Board {
  type Board = Array[Array[Int]]

  def prettyString(soduku: Board): String = { // soduku.map(row => row.mkString(" ")).mkString("\n") {
    val gridSeparator = List.fill(soduku(0).length / 3)("-------").mkString("+", "+", "+")

    soduku.grouped(3).map { threeRows =>
      threeRows.map { row =>
        row.grouped(3).map { columnGroup =>
          columnGroup.mkString(" ")
        }.mkString("| ", " | ", " |")
      }.mkString("\n")
    }.mkString(s"$gridSeparator\n", s"\n$gridSeparator\n", s"\n$gridSeparator")
  }
}
