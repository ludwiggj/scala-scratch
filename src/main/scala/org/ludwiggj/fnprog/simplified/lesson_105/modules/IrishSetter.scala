package org.ludwiggj.fnprog.simplified.lesson_105.modules

import java.awt.Color

trait Animal

abstract class AnimalWithTail(tailColor: Color)
  extends Animal

trait DogTailServices {
  this: AnimalWithTail =>
  def wagTail(): Unit = println("wagging tail")

  def lowerTail(): Unit = println("lowering tail")

  def raiseTail(): Unit = println("raising tail")
}

trait DogMouthServices {
  this: AnimalWithTail =>
  def bark(): Unit = println("bark!")

  def lick(): Unit = println("licking")
}

object IrishSetter
  extends AnimalWithTail(Color.red)
    with DogTailServices
    with DogMouthServices {

  def main(args: Array[String]): Unit = {
    this.bark()
    this.lowerTail()
  }
}