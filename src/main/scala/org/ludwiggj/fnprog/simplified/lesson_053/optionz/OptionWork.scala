package org.ludwiggj.fnprog.simplified.lesson_053.optionz

import scala.util.Try

object OptionWork {
  def intOption(s: String): Option[Int] = {
    def makeInt(s: String): Option[Int] = {
      try {
        Some(s.trim.toInt)
      } catch {
        case e: Exception => None
      }
    }

    makeInt(s) match {
      case Some(i) =>
        println(s"i = $i")
        Some(i)
      case None =>
        println(s"toInt could not parse [$s]")
        None
    }
  }

  def intTry(s: String): Try[Int] = {
    Try(s.trim.toInt)
  }

  def intEither(s: String): Either[String, Int] = {
    try {
      Right(s.trim.toInt)
    } catch {
      case e: Exception => Left(e.toString)
    }
  }

  def main(args: Array[String]): Unit = {
    // Option
    println(intOption("1"))
    println(intOption("wibble"))

    println(intOption("1").getOrElse(0))
    println(intOption("wibble").getOrElse(0))

    println(for {
      x <- intOption("1")
      y <- intOption("2")
      z <- intOption("3")
    } yield x + y + z)

    // Try
    println(intTry("1"))
    println(intTry("oops"))

    println(
      for {
        a <- intTry("1")
        b <- intTry("10")
      } yield a + b
    )

    println(intTry("1").getOrElse(0))
    println(intTry("oops").getOrElse(0))

    // Either
    println(intEither("1"))
    println(intEither("wibble"))

    println(
      for {
        a <- intEither("1")
        b <- intEither("10")
      } yield a + b
    )
  }
}