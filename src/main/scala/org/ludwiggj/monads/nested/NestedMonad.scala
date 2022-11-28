package org.ludwiggj.monads.nested

import scala.concurrent.{Await, Future}
import scala.language.implicitConversions
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object NestedMonad {
  def getIdByEmail(email: String): Future[Either[String, Int]] = Future {
    if (email.endsWith("@gmail.com")) {
      Right(1)
    } else if (email.endsWith("@yahoo.com")) {
      Right(2)
    } else {
      Left("No User with given email")
    }
  }

  def getPostsById(id: Int): Future[Either[String, List[String]]] = Future {
    if (id == 1) {
      Right(List("Post title 1", "Post title 2"))
    } else {
      Left("No posts found")
    }
  }

  def getPostsByEmail(email: String): Future[Product with Serializable] = {
    getIdByEmail(email).flatMap({
      case Right(id) =>
        getPostsById(id)
      case Left(error) => Future {
        Left(error)
      }
    })
  }

  def getPostsByEmailAlternative(email: String) = {
    // Doesn't work well...
    for {
      eitherAnIdValue <- getIdByEmail(email)
    } yield for {
      id <- eitherAnIdValue
    } yield for {
      posts <- getPostsById(id)
    } yield posts
  }

  def main(args: Array[String]): Unit = {
    //    println(Await.result(getPostsByEmail("mee@gmail.com"), 3.seconds))
    //    println(Await.result(getPostsByEmail("mee@bt.com"), 3.seconds))
    //    println(Await.result(getPostsByEmail("mee@yahoo.com"), 3.seconds))


    println(Await.result(getPostsByEmailAlternative("mee@gmail.com"), 3.seconds))
    println(Await.result(getPostsByEmailAlternative("mee@bt.com"), 3.seconds))
    println(Await.result(getPostsByEmailAlternative("mee@yahoo.com"), 3.seconds))
  }
}
