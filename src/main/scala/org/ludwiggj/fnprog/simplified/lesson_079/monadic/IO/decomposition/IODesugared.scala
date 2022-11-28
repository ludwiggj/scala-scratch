package org.ludwiggj.fnprog.simplified.lesson_079.monadic.IO.decomposition

case class IO[A](a: A) {
  def map[B](f: A => B): IO[B] = ???
  def flatMap[B](f: A => IO[B]): IO[B] = ???
  def run(): Unit = {}
}

object IODesugared {
  def main(args: Array[String]): Unit = {
    def getLine: IO[String] = IO(scala.io.StdIn.readLine())
    def putStrLn(s: String): IO[Unit] = IO(println(s))

    def doBlock(): IO[Unit] = for {
      _         <- putStrLn("First name?")
      firstName <- getLine
      _         <- putStrLn(s"first: $firstName")
    } yield ()

    doBlock().run()

    def doBlockDesugared(): IO[Unit] = putStrLn("First name?").flatMap {_ =>
      getLine.flatMap { firstName =>
        putStrLn(StringContext("first: ", "").s(firstName)).map { _ =>
          ()
        }
      }
    }

    doBlockDesugared().run()
  }
}