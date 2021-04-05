package org.ludwiggj.fnprog.simplified.lesson_094.stateT

import org.ludwiggj.fnprog.simplified.lesson_091.stateT.monads.{IO, Monad, StateT}

object StateTInForExpression {
  case class IntState(i: Int)

  // Add method that works with StateT

  // Remember StateT case class definition is StateT[M[_], S, A](run: S => M[(S, A)]) {..}
  // Substituting, StateT[IO, IntState, Int](run: IntState => IO[(IntState, Int)]) {..}
  def add(i: Int): StateT[IO, IntState, Int] = StateT[IO, IntState, Int] { oldState: IntState =>
    val newValue: Int = i + oldState.i
    val newState: IntState = oldState.copy(i = newValue)
    IO(newState, newValue)
  }

  // Same deal for the multiply function
  def multiply(i: Int): StateT[IO, IntState, Int] = StateT[IO, IntState, Int] { oldState: IntState =>
    val newValue = i * oldState.i
    val newState = oldState.copy(i = newValue)
    IO(newState, newValue)
  }

  def main(args: Array[String]): Unit = {
    // A StateT instance that add's 1 to something
    val a: StateT[IO, IntState, Int] = add(1)

    val b: IO[(IntState, Int)] = a.run(IntState(1))

    b.map(t => println(s"b state = ${t._1}")).run

    implicit val IOMonad: Monad[IO] = new Monad[IO] {
      def flatMap[A, B](ma: IO[A])(f: A => IO[B]): IO[B] = ma.flatMap(f)
      def lift[A](a: => A): IO[A] = IO(a)
    }

    // A StateT instance that adds 2, then adds 3, then multiplies by 10
    val forExpression: StateT[IO, IntState, Int] = for {
      _ <- add(2)
      _ <- add(3)
      x <- multiply(10)
    } yield x

    val result: IO[(IntState, Int)] = forExpression.run(IntState(1))

    result.map(tuple => println(s"result IntState = ${tuple._1}")).run
  }
}
