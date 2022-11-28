package org.ludwiggj.fnprog.simplified.lesson_080.io.monad.tryz

import org.ludwiggj.fnprog.simplified.lesson_076.io.monad.IOLazy

import scala.util.{Failure, Success, Try}

object IOWithTry {
  def readTextFileAsTry(filename: String): IOLazy[Try[List[String]]] = {
    IOLazy(Try {
      println(s"Attempting to get file $filename")
      val source = io.Source.fromFile(filename)
      (for (line <- source.getLines()) yield line).toList
    })
  }

  def main(args: Array[String]): Unit = {
    // aFile.txt exists at the top level
    val aFileThatExists: IOLazy[Try[List[String]]] = readTextFileAsTry("aFile.txt")
    val aFileThatDoesNotExist: IOLazy[Try[List[String]]] = readTextFileAsTry("anotherFile.txt")

    println("About to run...")

    runIt(aFileThatExists)
    runIt(aFileThatDoesNotExist)
  }

  private def runIt(task: IOLazy[Try[List[String]]]): Unit = {
    task.run match {
      case Success(lines) => lines.foreach(println)
      case Failure(s) => println(s"Failed, message is: $s")
    }
  }
}
