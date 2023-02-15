package org.ludwiggj.taglessfinal.testing.itv.news

import cats.syntax.all._
import cats.{MonadError, MonadThrow}
import mouse.all.FOptionSyntaxMouse
import org.ludwiggj.taglessfinal.testing.itv.news.Model.{ArticleEvent, ArticleNotFound}

trait ProcessArticle[F[_]] {
  def process(id: String): F[ArticleEvent]
}

object ProcessArticle {
  object UsingMonadThrow {
    def mkTake1[F[_] : MonadThrow : Log : ArticleRepo : ContentfulClient : ProduceEvent]: ProcessArticle[F] = (id: String) => for {
      maybeArticle <- ArticleRepo[F].get(id)
      _ <- MonadThrow[F].raiseWhen(maybeArticle.isEmpty)(new RuntimeException(s"Oh dear: ${ArticleNotFound(id)}"))
      article = maybeArticle.get
      relatedArticles <- ContentfulClient[F].articlesByTopic(article.topic)
      event = ArticleEvent(article, relatedArticles.filterNot(_.id == article.id))
      _ <- ProduceEvent[F].produce(event)
    } yield event

    // Similar to Dan T-B's original version that confused me :)

    // This is the magic sauce
    implicit class RichOption[A](opt: Option[A]) {
      def raiseError[F[_] : MonadThrow](t: Throwable): F[A] = opt match {
        case None => MonadThrow[F].raiseError(t)
        case Some(value) => value.pure[F]
      }
    }

    def mkTake2[F[_] : MonadThrow : Log : ArticleRepo : ContentfulClient : ProduceEvent]: ProcessArticle[F] = (id: String) => for {
        maybeArticle <- ArticleRepo[F].get(id)
        // NOTE: raiseError is overloaded here (possibly a bad idea in terms of understandability?)
        article <- maybeArticle.raiseError(new RuntimeException(s"Oh dear: ${ArticleNotFound(id)}"))
        relatedArticles <- ContentfulClient[F].articlesByTopic(article.topic)
        event = ArticleEvent(article, relatedArticles.filterNot(_.id == article.id))
        _ <- ProduceEvent[F].produce(event)
      } yield event
  }

  object UsingMonadError {
    type ArticleError[F[_]] = MonadError[F, ArticleNotFound]

    def mkTake1[F[_] : ArticleError : Log : ArticleRepo : ContentfulClient : ProduceEvent]: ProcessArticle[F] = (id: String) => for {
      maybeArticle <- ArticleRepo[F].get(id)
      article <- maybeArticle match {
        case Some(anArticle) => anArticle.pure

        // Results in compilation error - not found: value ArticleError
        // This is because it's a type alias! (no apply method available)
        // case None => ArticleError[F].raiseError(ArticleNotFound(id))

        case None => ArticleNotFound(id).raiseError
      }
      relatedArticles <- ContentfulClient[F].articlesByTopic(article.topic)
      event = ArticleEvent(article, relatedArticles.filterNot(_.id == article.id))
      _ <- ProduceEvent[F].produce(event)
    } yield event

    // Using MonadError[F, ArticleNotFound].raiseError instead
    def mkTake2[F[_] : ArticleError: Log : ArticleRepo : ContentfulClient : ProduceEvent]: ProcessArticle[F] = (id: String) => for {
      maybeArticle <- ArticleRepo[F].get(id)
      article <- maybeArticle match {
        case Some(anArticle) => anArticle.pure
        case None => MonadError[F, ArticleNotFound].raiseError(ArticleNotFound(id))
      }
      relatedArticles <- ContentfulClient[F].articlesByTopic(article.topic)
      event = ArticleEvent(article, relatedArticles.filterNot(_.id == article.id))
      _ <- ProduceEvent[F].produce(event)
    } yield event

    // Using explicit parameter for ArticleError
    def mkTake3[F[_] : Log : ArticleRepo : ContentfulClient : ProduceEvent](implicit em: ArticleError[F]): ProcessArticle[F] = (id: String) => for {
      maybeArticle <- ArticleRepo[F].get(id)
      article <- maybeArticle match {
        case Some(anArticle) => anArticle.pure
        case None => em.raiseError(ArticleNotFound(id))
      }
      relatedArticles <- ContentfulClient[F].articlesByTopic(article.topic)
      event = ArticleEvent(article, relatedArticles.filterNot(_.id == article.id))
      _ <- ProduceEvent[F].produce(event)
    } yield event

    // Using fromOption. NOTE: This has exactly same implementation as my own-rolled version, see take 1 above :)
    def mkTake4[F[_] : ArticleError : Log : ArticleRepo : ContentfulClient : ProduceEvent]: ProcessArticle[F] = (id: String) => for {
      maybeArticle <- ArticleRepo[F].get(id)
      article <- MonadError[F, ArticleNotFound].fromOption(maybeArticle, ArticleNotFound(id))
      relatedArticles <- ContentfulClient[F].articlesByTopic(article.topic)
      event = ArticleEvent(article, relatedArticles.filterNot(_.id == article.id))
      _ <- ProduceEvent[F].produce(event)
    } yield event

    // Using getOrRaise method from mouse (https://github.com/typelevel/mouse)
    def mkTake5[F[_] : ArticleError : Log : ArticleRepo : ContentfulClient : ProduceEvent]: ProcessArticle[F] = (id: String) => for {
      article <- ArticleRepo[F].get(id).getOrRaise(ArticleNotFound(id))
      relatedArticles <- ContentfulClient[F].articlesByTopic(article.topic)
      event = ArticleEvent(article, relatedArticles.filterNot(_.id == article.id))
      _ <- ProduceEvent[F].produce(event)
    } yield event
  }
}
