package org.ludwiggj.fnprog.simplified.lesson_089.iostate

import org.ludwiggj.fnprog.simplified.lesson_085.state.monad.State

object TryStateAndIoTogether extends App {
  // State code
  type Stack = List[String]

  def push(x: String): State[Stack, Unit] = State[Stack, Unit] {
    xs => (x :: xs, ())
  }

  // IO functions
  def getLine: IO[String] = IO(scala.io.StdIn.readLine())

  def putStrLn(s: String): IO[Unit] = IO(println(s))

  // main loop: Prompt a user for some input,
  // then push that input onto a stack
//    val res = for {
//      _ <- putStrLn("Type anything:") //IO
//      input <- getLine //IO
//      _ <- push(input) //State
//      _ <- putStrLn(s"Input: $input") //IO
//    } yield ()

  // Flatmap and map version
//  val res =
//    putStrLn("Type anything:").flatMap({ _ =>
        // This flatMap expects String => IO[B], but is being passed String => State[S,B]
//        getLine.flatMap(input =>
          // This flatMap expects Unit => State[S,B], but is being passed String => IO[B]
//          push(input).flatMap { _ =>
//              putStrLn(s"Input: $input").map { _ => () }
//            }
//        )
//      })
}