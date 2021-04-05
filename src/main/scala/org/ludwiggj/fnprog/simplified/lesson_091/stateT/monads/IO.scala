package org.ludwiggj.fnprog.simplified.lesson_091.stateT.monads

class IO[A] private(constructorCodeBlock: => A) {
  def run: A = constructorCodeBlock

  def flatMap[B](f: A => IO[B]): IO[B] = f(run)

  def map[B](f: A => B): IO[B] = IO(f(run))

  // Alternative definition from https://github.com/jdegoes/lambdaconf-2014-introgame
//   def flatMap[B](f: A => IO[B]): IO[B] = IO(f(run).run)
//   def map[B](f: A => B): IO[B] = flatMap(a => IO(f(a)))
}

object IO {
  def apply[A](a: => A): IO[A] = new IO(a)
}
