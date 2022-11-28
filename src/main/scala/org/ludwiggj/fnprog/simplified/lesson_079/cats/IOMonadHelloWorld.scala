package org.ludwiggj.fnprog.simplified.lesson_079.cats

import cats.effect.IO
import cats.effect.unsafe.implicits.global

object IOMonadHelloWorld extends App {
  val hello: IO[Unit] = IO { println("Hello, world") }

  hello.unsafeRunSync()
}