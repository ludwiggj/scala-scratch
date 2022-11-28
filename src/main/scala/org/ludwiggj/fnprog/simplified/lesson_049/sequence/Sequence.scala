package org.ludwiggj.fnprog.simplified.lesson_049.sequence

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
    Sequence(abMap.toSeq: _*)
  }

  def withFilter(p: A => Boolean): Sequence[A] = {
    val tmpArrayBuffer = elems.filter(p)
    Sequence(tmpArrayBuffer.toSeq: _*)
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    val strings = Sequence("a", "b", "c")
    
    for (s <- strings) {
      println(s)
    }

    val ints = Sequence(1, 2, 3, 4, 5)
    println(for {
      i <- ints
      if i > 2
    } yield i*2)
  }
}
