package org.ludwiggj.fnprog.simplified.lesson_050.sequence

import scala.collection.mutable.ArrayBuffer

case class Sequence[A](initialElems: A*) {
  // this is a book, don't do this at home
  private val elems = scala.collection.mutable.ArrayBuffer[A]()
  // initialize
  elems ++= initialElems

  // (1) Supports use as a generator in a simple for loop
  def foreach(block: A => Unit): Unit = {
    elems.foreach(block)
  }

  // (2) Supports use as a generator in a for loop with a yield
  def map[B](f: A => B): Sequence[B] = {
    val abMap: ArrayBuffer[B] = elems.map(f)
    Sequence(abMap.toSeq: _*)
  }

  // NOTE: (1) and (2) are independent

  // (3) Supports use of if in for loop
  def withFilter(p: A => Boolean): Sequence[A] = {
    val tmpArrayBuffer = elems.filter(p)
    Sequence(tmpArrayBuffer.toSeq: _*)
  }

  // (4) Supports use of second generator in for loop
  def flatMap[B](f: A => Sequence[B]): Sequence[B] = {
    val mapRes: Sequence[Sequence[B]] = map(f) //map
    flattenLike(mapRes)                            //flatten
  }

  def flattenLike[B](seqOfSeq: Sequence[Sequence[B]]): Sequence[B] = {
    var xs = ArrayBuffer[B]()
    for (listB: Sequence[B] <- seqOfSeq) {
      for (e <- listB) {
        xs += e
      }
    }
    Sequence(xs.toSeq: _*)
  }
}

case class Person(name: String)

object Workout {
  def main(args: Array[String]): Unit = {
    val strings = Sequence("a", "b", "c")
    println(strings)

    // This is fixed by (1)
    val nums = Sequence(1, 2, 3, 4, 5)
    println(nums)

    for (i <- nums) println(i)

    // This is fixed by (2)
    println(for {
      i <- nums
    } yield i * 2)

    // This is fixed by (3)
    println(for {
      i <- nums
      if i > 2
    } yield i * 2)

    val myFriends = Sequence(
      Person("Adam"),
      Person("David"),
      Person("Frank")
    )
    val adamsFriends = Sequence(
      Person("Nick"),
      Person("David"),
      Person("Frank")
    )

    // This is fixed by (4)
    val mutualFriends = for {
      myFriend <- myFriends // generator
      adamsFriend <- adamsFriends // generator
      if myFriend.name == adamsFriend.name
    } yield myFriend

    mutualFriends.foreach(println)

    // map vs. flatmap
    val fruits = Seq("apple", "banana", "orange")

    println(s"Map:\n${fruits.map(_.toUpperCase)}")
    println(s"FlatMap:\n${fruits.flatMap(_.toUpperCase)}")
  }
}