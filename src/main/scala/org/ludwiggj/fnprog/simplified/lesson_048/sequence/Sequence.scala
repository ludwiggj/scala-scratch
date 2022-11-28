package org.ludwiggj.fnprog.simplified.lesson_048.sequence

import scala.collection.mutable.ArrayBuffer

case class Sequence[A](initialElems: A*) {
  // this is a book, don't do this at home
  private val elems = scala.collection.mutable.ArrayBuffer[A]()

  // initialize
  elems ++= initialElems

  def foreach(block: A => Unit): Unit = {
    elems.foreach(block)
  }

  def map[B](f: A => B): Sequence[B] = {
    val abMap: ArrayBuffer[B] = elems.map(f)

    Sequence[B](abMap.toSeq:_*)
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    val strings = Sequence("a", "b", "c")
    
    for (s <- strings) {
      println(s)
    }

    val ints = Sequence(1,2,3)
    println(for {
      i <- ints
    } yield i*2)
  }
}
