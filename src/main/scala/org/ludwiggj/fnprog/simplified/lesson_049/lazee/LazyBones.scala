package org.ludwiggj.fnprog.simplified.lesson_049.lazee

// Taken from https://alvinalexander.com/scala/examples-shows-differences-between-strict-lazy-evaluation-in-scala
object LazyBones {
  def main(args: Array[String]): Unit = {
    def lessThan30(i: Int): Boolean = {
      println(s"\n$i less than 30?")
      i < 30
    }

    def moreThan20(i: Int): Boolean = {
      println(s"$i more than 20?")
      i > 20
    }

    def lazyRun(): Unit = {
      println("Lazy run...")
      println("Filter applied to each element in turn, when required...")
      val a = List(1, 25, 40, 5, 23)
      val q0 = a.withFilter(lessThan30)
      val q1 = q0.withFilter(moreThan20)
      for (r <- q1) println(s"$r")
    }

    def strictRun(): Unit = {
      println("Strict run...")
      println("Filter applied to all elements of first list...")
      println("Then filter applied to all elements of second list...")
      val a = List(1, 25, 40, 5, 23)
      val q0 = a.filter(lessThan30)
      val q1 = q0.filter(moreThan20)
      for (r <- q1) println(s"$r")
    }

    // https://docs.scala-lang.org/tutorials/FAQ/yield.html
    def listWithStrictFilterIsStrict(): Unit = {
      println("List with strict filter is strict")
      var found = false
      List.range(1,10).filter(_ % 2 == 1 && !found).foreach(x => if (x == 5) found = true else println(x))
    }

    def streamWithStrictFilterIsLazy(): Unit = {
      println("Stream with strict filter is lazy")
      var found = false
      LazyList.range(1,10).filter(_ % 2 == 1 && !found).foreach(x => if (x == 5) found = true else println(x))
    }

    def listWithLazyFilterIsLazy(): Unit = {
      println("List with lazy filter is lazy")
      var found = false
      List.range(1,10).withFilter(_ % 2 == 1 && !found).foreach(x => if (x == 5) found = true else println(x))
    }

    def streamWithLazyFilterIsLazy(): Unit = {
      println("Stream with lazy filter is lazy")
      var found = false
      LazyList.range(1,10).withFilter(_ % 2 == 1 && !found).foreach(x => if (x == 5) found = true else println(x))
    }

    lazyRun()
    strictRun()

    listWithStrictFilterIsStrict()
    streamWithStrictFilterIsLazy()
    listWithLazyFilterIsLazy()
    streamWithLazyFilterIsLazy()
  }
}
