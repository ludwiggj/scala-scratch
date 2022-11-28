package org.ludwiggj.fnprog.simplified.lesson_078.io.monad.recursive

import org.ludwiggj.fnprog.simplified.lesson_076.io.monad.{IOEager, IOLazy, eager, lazee}

object FPRecursiveLoop extends App {

  def lazyLoop(): Unit = {
    import lazee._
    def loop: IOLazy[Unit] = for {
      _ <- putStrLn("Type something: ")
      input <- getLine
      _ <- putStrLn(s"You said '$input'.")
      _ <- if (input == "quit") IOLazy(()) else loop //RECURSE
    } yield ()

    loop.run
  }

  def eagerLoop(): Unit = {
    import eager._
    def loop: IOEager[Unit] = for {
      _ <- putStrLn("Oy! Type something: ")
      input <- getLine
      _ <- putStrLn(s"You spit '$input'.")
      _ <- if (input == "quit") IOEager(()) else loop //RECURSE
    } yield ()

    loop
  }

  def eagerLoopDesugared(): Unit = {
    import eager._
    def loop: IOEager[Unit] = eager.putStrLn("Oy! Type something: ")
      .flatMap({ _ =>
        getLine
          .flatMap(input => eager.putStrLn(s"You spit '$input'.")
            .flatMap { _ =>
              (if (input == "quit") IOEager(()) else loop)
                .map(_ => ())
            }
          )
      })

    loop
  }

  lazyLoop()
  //  eagerLoop()
  //  eagerLoopDesugared()
}