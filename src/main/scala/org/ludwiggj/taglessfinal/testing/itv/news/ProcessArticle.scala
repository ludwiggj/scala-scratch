package org.ludwiggj.taglessfinal.testing.itv.news

import cats.{Applicative, MonadError, MonadThrow}
import cats.syntax.all._
import mouse.all.FOptionSyntaxMouse
import org.ludwiggj.taglessfinal.testing.itv.news.Model.{ArticleEvent, ArticleNotFound}

trait ProcessArticle[F[_]] {
  def process(id: String): F[ArticleEvent]
}

object ProcessArticle {
  def mkWithMonadThrow[F[_] : MonadThrow : Log : ArticleRepo : ContentfulClient : ProduceEvent]: ProcessArticle[F] = (id: String) => for {
    maybeArticle <- ArticleRepo[F].get(id)
    _ <- MonadThrow[F].raiseWhen(maybeArticle.isEmpty)(new RuntimeException(s"Oh dear: ${ArticleNotFound(id)}"))
    article = maybeArticle.get
    relatedArticles <- ContentfulClient[F].articlesByTopic(article.topic)
    event = ArticleEvent(article, relatedArticles.filterNot(_.id == article.id))
    _ <- ProduceEvent[F].produce(event)
  } yield event

  // Similar to Dan T-B's original version that confused me :)
  def mkWithMonadOriginal[F[_] : MonadThrow : Log : ArticleRepo : ContentfulClient : ProduceEvent]: ProcessArticle[F] = (id: String) => {
    implicit class RichOption[A](opt: Option[A]) {
      def raiseError[F[_] : MonadThrow](t: Throwable): F[A] = opt match {
        case None => MonadThrow[F].raiseError(t)
        case Some(value) => value.pure[F]
      }
    }

    for {
      maybeArticle <- ArticleRepo[F].get(id)
      _ <- maybeArticle.raiseError(new RuntimeException(s"Oh dear: ${ArticleNotFound(id)}"))
      article = maybeArticle.get
      relatedArticles <- ContentfulClient[F].articlesByTopic(article.topic)
      event = ArticleEvent(article, relatedArticles.filterNot(_.id == article.id))
      _ <- ProduceEvent[F].produce(event)
    } yield event
  }

  type ArticleError[F[_]] = MonadError[F, ArticleNotFound]

  // Using context bounds
  def mkWithArticleNotFoundError[F[_] : ArticleError : Log : ArticleRepo : ContentfulClient : ProduceEvent]: ProcessArticle[F] = (id: String) => for {
    maybeArticle <- ArticleRepo[F].get(id)
    article <- maybeArticle match {
      case Some(value) => value.pure
      // Results in compilation error - not found: value ArticleError
      // This is because it's a type alias! (no apply method available)
      // case None => ArticleError[F].raiseError(ArticleNotFound(id))
      case None => ArticleNotFound(id).raiseError
    }
    _ <- MonadError[F, ArticleNotFound].fromOption(maybeArticle, ArticleNotFound(id))
    relatedArticles <- ContentfulClient[F].articlesByTopic(article.topic)
    event = ArticleEvent(article, relatedArticles.filterNot(_.id == article.id))
    _ <- ProduceEvent[F].produce(event)
  } yield event

  def mkWithArticleNotFoundError2[F[_] : ArticleError : Log : ArticleRepo : ContentfulClient : ProduceEvent]: ProcessArticle[F] = (id: String) => for {
    maybeArticle <- ArticleRepo[F].get(id)
    // NOTE: This has exactly same implementation as my own-rolled version :)
    article <- MonadError[F, ArticleNotFound].fromOption(maybeArticle, ArticleNotFound(id))
    relatedArticles <- ContentfulClient[F].articlesByTopic(article.topic)
    event = ArticleEvent(article, relatedArticles.filterNot(_.id == article.id))
    _ <- ProduceEvent[F].produce(event)
  } yield event

  // Using getOrRaise method from mouse (https://github.com/typelevel/mouse)
  def mkWithArticleNotFoundError3[F[_] : ArticleError : Log : ArticleRepo : ContentfulClient : ProduceEvent]: ProcessArticle[F] = (id: String) => for {
    article <- ArticleRepo[F].get(id).getOrRaise(ArticleNotFound(id))
    relatedArticles <- ContentfulClient[F].articlesByTopic(article.topic)
    event = ArticleEvent(article, relatedArticles.filterNot(_.id == article.id))
    _ <- ProduceEvent[F].produce(event)
  } yield event

  // Using explicit parameter for ArticleError
  def mkWithArticleNotFoundError4[F[_] : Applicative : Log : ArticleRepo : ContentfulClient : ProduceEvent](implicit em: ArticleError[F]): ProcessArticle[F] = (id: String) => for {
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
