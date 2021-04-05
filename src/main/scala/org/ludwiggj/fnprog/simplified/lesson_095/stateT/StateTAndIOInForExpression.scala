package org.ludwiggj.fnprog.simplified.lesson_095.stateT

import org.ludwiggj.fnprog.simplified.lesson_091.stateT.monads.{IO, Monad, StateT}

object StateTAndIOInForExpression {
  def getLine: IO[String] = IO(scala.io.StdIn.readLine())

  def putStr(s: String): IO[Unit] = IO(print(s))

  def liftIoIntoStateT[A](io: IO[A]): StateT[IO, SumState, A] = StateT { s: SumState =>
    io.map(a => (s, a))
  }

  def getLineAsStateT: StateT[IO, SumState, String] = liftIoIntoStateT(getLine)

  def putStrAsStateT(s: String): StateT[IO, SumState, Unit] = liftIoIntoStateT(putStr(s))

  def liftIoIntoStateTWithDebug[A](io: IO[A]): StateT[IO, SumState, A] = StateT { s: SumState =>
    val result = io.map(a => (s, a))
    result.map {
      tup =>
        println(s"lift: (${tup._1}, ${tup._2})")
        tup
    }
  }

  def toInt(s: String): Int = {
    try {
      s.toInt
    } catch {
      case e: NumberFormatException => 0
    }
  }

  implicit val IOMonad: Monad[IO] = new Monad[IO] {
    def flatMap[A, B](ma: IO[A])(f: A => IO[B]): IO[B] = ma.flatMap(f)

    def lift[A](a: => A): IO[A] = IO(a)
  }

  case class SumState(sum: Int)

  // This is exactly the same as the add function in the previous lesson, lesson 94
  def updateAppState(newValue: Int): StateT[IO, SumState, Int] = StateT { oldState: SumState =>
    // create a new sum from `i` and the previous sum from `s`
    val oldSum = oldState.sum
    val newSum = newValue + oldSum

    println(s"updateAppState, old sum: $oldSum")
    println(s"updateAppState, new input: $newValue")
    println(s"updateAppState, new sum: $newSum")

    // create a new SumState
    val newState: SumState = oldState.copy(sum = newSum)

    // return the new state and the new sum, wrapped in an IO
    IO(newState, newSum)
  }

  def debugLiftIOIntoStateT(): Unit = {
    val i: IO[Int] = IO(1)
    val stateT = liftIoIntoStateTWithDebug(i)
    println(stateT.run(SumState(2)).run)
  }

  // Doesn't compile, due to mix of output types

  //  def sumLoop: StateT[IO, SumState, Unit] = for {
  //    _ <- putStr("\ngive me an int: ")
  //    input <- getLine
  //    i <- IO(toInt(input))
  //    _ <- updateAppState(i)
  //    _ <- sumLoop
  //  } yield Unit

  // Lesson 97
  def sumLoopWithoutQuitOption: StateT[IO, SumState, Unit] = for {
    _ <- putStrAsStateT("\ngive me an int: ")
    input <- getLineAsStateT
    i <- liftIoIntoStateT(IO(toInt(input)))
    _ <- updateAppState(i)
    _ <- sumLoopWithoutQuitOption
  } yield Unit

  def runSumLoopWithoutQuitOption(): Unit = {
    val resultIO: IO[(SumState, Unit)] = sumLoopWithoutQuitOption.run(SumState(0))
    val result: (SumState, Unit) = resultIO.run
  }

  // Lesson 98
  def sumLoop: StateT[IO, SumState, Unit] = for {
    _ <- putStrAsStateT("\ngive me an int: ")
    input <- getLineAsStateT
    _ <- if (input == "q") {
      liftIoIntoStateT(IO(Unit))
    } else for {
      i <- liftIoIntoStateT(IO(toInt(input)))
      _ <- updateAppState(i)
      _ <- sumLoop
    } yield Unit
  } yield Unit

  def runSumLoop(): Unit = {
    val result: (SumState, Unit) = sumLoop.run(SumState(0)).run
    println(s"Final SumState: $result")
  }

  def main(args: Array[String]): Unit = {
//    debugLiftIOIntoStateT()
//    runSumLoopWithoutQuitOption()
    runSumLoop()
  }
}