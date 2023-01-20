package org.ludwiggj.taglessfinal.testing.itv.news

import cats.{Applicative, MonadError, MonadThrow}
import cats.syntax.all._
import org.ludwiggj.taglessfinal.testing.itv.news.Model.{ArticleEvent, ArticleNotFound}

trait ProcessArticle[F[_]] {
  def process(id: String): F[ArticleEvent]
}

object ProcessArticle {
  def mk[F[_] : MonadThrow : Log : ArticleRepo : ContentfulClient : ProduceEvent]: ProcessArticle[F] = (id: String) => for {
    maybeArticle <- ArticleRepo[F].get(id)
    article <- maybeArticle match {
      case Some(value) => value.pure
      // This works ok
      // case None => MonadThrow[F].raiseError(new RuntimeException(s"Oh dear: ${ArticleNotFound(id)}"))
      case None => new RuntimeException(s"Oh dear: ${ArticleNotFound(id)}").raiseError
    }
    relatedArticles <- ContentfulClient[F].articlesByTopic(article.topic)
    event = ArticleEvent(article, relatedArticles.filterNot(_.id == article.id))
    _ <- ProduceEvent[F].produce(event)
  } yield event

  type ArticleError[F[_]] = MonadError[F, ArticleNotFound]

  def mkTake2[F[_] : ArticleError : Log : ArticleRepo : ContentfulClient : ProduceEvent]: ProcessArticle[F] = (id: String) => for {
    maybeArticle <- ArticleRepo[F].get(id)
    article <- maybeArticle match {
      case Some(value) => value.pure
      // Results in compilation error - not found: value ArticleError
      // case None => ArticleError[F].raiseError(ArticleNotFound(id)) // DOESN'T WORK
      case None => ArticleNotFound(id).raiseError
    }
    relatedArticles <- ContentfulClient[F].articlesByTopic(article.topic)
    event = ArticleEvent(article, relatedArticles.filterNot(_.id == article.id))
    _ <- ProduceEvent[F].produce(event)
  } yield event

  def mkTake3[F[_] : Applicative : Log : ArticleRepo : ContentfulClient : ProduceEvent](implicit em: ArticleError[F]): ProcessArticle[F] = (id: String) => for {
    maybeArticle <- ArticleRepo[F].get(id)
    article <- maybeArticle match {
      case Some(value) => value.pure
      case None => em.raiseError(ArticleNotFound(id))
    }
    relatedArticles <- ContentfulClient[F].articlesByTopic(article.topic)
    event = ArticleEvent(article, relatedArticles.filterNot(_.id == article.id))
    _ <- ProduceEvent[F].produce(event)
  } yield event
}
