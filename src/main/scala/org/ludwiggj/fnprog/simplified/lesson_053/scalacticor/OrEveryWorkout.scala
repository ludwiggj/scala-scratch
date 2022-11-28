package org.ludwiggj.fnprog.simplified.lesson_053.scalacticor

import org.scalactic._

// See http://www.scalactic.org/user_guide/OrAndEvery
object OrEveryWorkout {

  case class Person(name: String, age: Int)

  def optionExample(): Unit = {
    def parseName(input: String): Option[String] = {
      val trimmed = input.trim
      if (!trimmed.isEmpty) Some(trimmed) else None
    }

    def parseAge(input: String): Option[Int] = {
      try {
        val age = input.trim.toInt
        if (age >= 0) Some(age) else None
      }
      catch {
        case _: NumberFormatException => None
      }
    }

    def parsePerson(inputName: String, inputAge: String): Option[Person] =
      for {
        name <- parseName(inputName)
        age <- parseAge(inputAge)
      } yield Person(name, age)

    println(parsePerson("Bridget Jones", "29"))
    // Result: Some(Person(Bridget Jones,29))

    println(parsePerson("Bridget Jones", ""))
    // Result: None

    println(parsePerson("Bridget Jones", "-29"))
    // Result: None

    println(parsePerson("", ""))
    // Result: None
  }

  def eitherExample(): Unit = {
    def parseName(input: String): Either[String, String] = {
      val trimmed = input.trim
      if (!trimmed.isEmpty) Right(trimmed) else Left(s""""$input" is not a valid name""")
    }

    def parseAge(input: String): Either[String, Int] = {
      try {
        val age = input.trim.toInt
        if (age >= 0) Right(age) else Left(s""""$age" is not a valid age""")
      }
      catch {
        case _: NumberFormatException => Left(s""""$input" is not a valid integer""")
      }
    }

    def parsePerson(inputName: String, inputAge: String): Either[String, Person] =
      for {
        name <- parseName(inputName)
        age <- parseAge(inputAge)
      } yield Person(name, age)

    println(parsePerson("Bridget Jones", "29"))
    // Result: Right(Person(Bridget Jones,29))

    println(parsePerson("Bridget Jones", ""))
    // Result: Left("" is not a valid integer)

    println(parsePerson("Bridget Jones", "-29"))
    // Result: Left("-29" is not a valid age)

    println(parsePerson("", ""))
    // Result: Left("" is not a valid name)
  }

  def orExample(): Unit = {
    type ErrorMessage = String

    def parseName(input: String): String Or ErrorMessage = {
      val trimmed = input.trim
      if (!trimmed.isEmpty) Good(trimmed) else Bad(s""""$input" is not a valid name""")
    }

    def parseAge(input: String): Int Or ErrorMessage = {
      try {
        val age = input.trim.toInt
        if (age >= 0) Good(age) else Bad(s""""$age" is not a valid age""")
      }
      catch {
        case _: NumberFormatException => Bad(s""""$input" is not a valid integer""")
      }
    }

    def parsePerson(inputName: String, inputAge: String): Person Or ErrorMessage =
      for {
        name <- parseName(inputName)
        age <- parseAge(inputAge)
      } yield Person(name, age)

    println(parsePerson("Bridget Jones", "29"))
    // Result: Good(Person(Bridget Jones,29))

    println(parsePerson("Bridget Jones", ""))
    // Result: Bad("" is not a valid integer)

    println(parsePerson("Bridget Jones", "-29"))
    // Result: Bad("-29" is not a valid age)

    println(parsePerson("", ""))
    // Result: Bad("" is not a valid name)
  }

  def orEveryExample(): Unit = {
    type ErrorMessage = String

    def parseName(input: String): String Or One[ErrorMessage] = {
      val trimmed = input.trim
      if (!trimmed.isEmpty) Good(trimmed) else Bad(One(s""""$input" is not a valid name"""))
    }

    def parseAge(input: String): Int Or One[ErrorMessage] = {
      try {
        val age = input.trim.toInt
        if (age >= 0) Good(age) else Bad(One(s""""$age" is not a valid age"""))
      }
      catch {
        case _: NumberFormatException => Bad(One(s""""$input" is not a valid integer"""))
      }
    }

    import Accumulation._

    def parsePerson(inputName: String, inputAge: String): Person Or Every[ErrorMessage] = {
      val name = parseName(inputName)
      val age = parseAge(inputAge)
      withGood(name, age) {
        Person
      }
    }

    println(parsePerson("Bridget Jones", "29"))
    // Result: Good(Person(Bridget Jones,29))

    println(parsePerson("Bridget Jones", ""))
    // Result: Bad(One("" is not a valid integer))

    println(parsePerson("Bridget Jones", "-29"))
    // Result: Bad(One("-29" is not a valid age))

    println(parsePerson("", ""))
    // Result: Bad(Many("" is not a valid name, "" is not a valid integer))

    println(One(1))
    println(Many(1, 3))
    println(Many(1, 2, 3))
    println(Every(1))
    println(Every(1, 2))
    println(Every(1, 2, 3))
  }

  def main(args: Array[String]): Unit = {
    optionExample()
    eitherExample()
    orExample()
    orEveryExample()
  }
}