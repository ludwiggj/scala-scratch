package org.ludwiggj.fnprog.simplified.lesson_055.curlybrace

object Anonymous {

  def main(args: Array[String]): Unit = {
    // DEFINITION 1: Given the following definition:

    // val x = FOO {
    //  // more code here
    // }

    // POSSIBILITY 1: An anonymous class

    trait PersonTrait {
      def name: String
      def age: Int
      override def toString = s"name: $name, age: $age"
    }

    // This doesn't exactly match because of new operator
    val mary: PersonTrait = new PersonTrait {
      val name = "mary"
      val age = 22
    }

    println(s"Mary: $mary")

    // Given the above declaration, FOO/Person cannot be a case class
    case class PersonCaseClass(name: String, age: Int)

    // because:

    // (i) Case class isn't created with the new keyword

    //  val jose1 = new PersonCaseClass {
    //    val name = "jose1"
    //    val age = 29
    //  }

    // (ii) Case class must pass at least one value into constructor (following statement doesn't compile)

    //  val jose2 = PersonCaseClass {
    //    val name = "jose2"
    //    val age = 81
    //  }

    // Person could be a normal class
    class PersonClass {
      val name: String = ""
      def age: Int = 0
      override def toString = s"name: $name, age: $age"
    }

    // If Person is a class with name and age fields (as above) those fields
    // need an override qualifier when the instance is created
    val jen: PersonClass = new PersonClass {
      override val name = "jen"
      override val age = 22
    }

    println(s"Jen: $jen")

    // It does work if the class declaration does not have name and age fields
    class AnotherPersonClass

    val burt: AnotherPersonClass = new AnotherPersonClass {
      val name = "burt"
      val age = 25
      override def toString = s"name: $name, age: $age"
    }

    println(s"Burt: $burt")

    // Could also be an abstract class
    abstract class PersonAbstractClass {
      val name: String
      def age: Int
      override def toString = s"name: $name, age: $age"
    }

    val ernie: PersonAbstractClass = new PersonAbstractClass {
      val name = "ernie"
      val age = 15
    }

    println(s"Ernie: $ernie")

    // POSSIBILITY 2: A function that takes a by-name parameter

    def timer[A](blockOfCode: => A) = {
      val startTime = System.nanoTime
      val result = blockOfCode
      val stopTime = System.nanoTime
      val delta = stopTime - startTime
      (result, delta/1000000d)
    }

    val (result, time) = timer {
      Thread.sleep(1000)
      42
    }

    println(s"Result $result, time $time")
  }
}