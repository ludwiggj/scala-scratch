package org.ludwiggj.taglessfinal.testing.itv.news

object Stuff {
  /*
  def divide[F[_]](dividend: Int, divisor: Int)(implicit F: ApplicativeError[F, ArticleNotFound]): F[Int] =
    if (divisor === 0) F.raiseError(ArticleNotFound("division by zero"))
    else F.pure(dividend / divisor)

  def divide2[F[_]](dividend: Int, divisor: Int)(implicit F: ApplicativeError[F, Throwable]): F[Int] = {
    Try {
      dividend / divisor
    }.toEither match {
      case Left(_) => F.raiseError(new Throwable("aargh!"))
      case Right(value) => F.pure(value)
    }
    // .raiseError(new Throwable("aargh!"))
    //      .raiseError(ArticleNotFound("division by zero"))
  }

  type ErrorOr[A] = Either[ArticleNotFound, A]

  divide[ErrorOr](6, 3)
  */

  /*
  //        article <- maybeArticle match {
//          case Some(value) => F.pure(value)
//          case None => F.raiseError(ArticleNotFound(id))
//        }
//         article <- maybeArticle.raiseError(ArticleNotFound(id))
         // article <- maybeArticle.raiseError(new RuntimeException(s"Oh dear: ${ArticleNotFound(id)}"))
         // _ <- MonadThrow[F].raiseError[Option[Article]](new RuntimeException(s"Oh dear: ${ArticleNotFound(id)}"))
//        relatedArticles <- ContentfulClient[F].articlesByTopic(article.topic)
//        event = ArticleEvent(article, relatedArticles.filterNot(_.id == article.id))
//        _ <- ProduceEvent[F].produce(event)
//      } yield event


//  val applErrorVal: ApplicativeError[F, ArticleNotFound] = ApplicativeError[F, ArticleNotFound]
//  val applErrorVal2: ApplicativeError[F, String] = ApplicativeError[F, String]

  // def applErrorVal[F[_]]: ApplicativeError[F, ArticleNotFound] = ApplicativeError[F, ArticleNotFound]
//  def mk[F[_] : MonadThrow : Log : ArticleRepo : ContentfulClient : ProduceEvent](implicit F: ApplicativeError[F, ArticleNotFound]): ProcessArticle[F] = new ProcessArticle[F] {

    // Type of maybeArticle is Option[Model.Article]
    // error = Option.empty[Model.Article]
    // _ <- maybeArticle.raiseError(error)
    // _ <- maybeArticle.raiseError(ArticleNotFound(id))
   */
}
