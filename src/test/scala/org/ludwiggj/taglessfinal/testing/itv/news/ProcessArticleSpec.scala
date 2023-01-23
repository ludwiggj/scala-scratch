package org.ludwiggj.taglessfinal.testing.itv.news

import cats.implicits.{catsSyntaxApplicativeErrorId, catsSyntaxEitherId, catsSyntaxOptionId}
import org.ludwiggj.taglessfinal.testing.itv.news.Model.{Article, ArticleEvent}
import org.ludwiggj.taglessfinal.testing.itv.news.ProcessArticle.ArticleError
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ProcessArticleSpec extends AnyFlatSpec with Matchers  {
   trait Fixture {
     type F[A] = Either[Throwable, A]

     val articleId: String = "20230120_001"
     val article: Article = Article(id = articleId, title = "How to test tagless final", topic = "Testing")

     val expected: ArticleEvent = ArticleEvent(article, Nil)

     implicit val log: Log[F] = _ => ().asRight
     implicit val repo: ArticleRepo[F] = _ => article.some.asRight
     implicit val contentfulClient: ContentfulClient[F] = _ => List.empty.asRight
     implicit val eventProducer: ProduceEvent[F] = _ => ().asRight
   }

  trait FixtureWithMonadError extends Fixture {
    // This is a bit clumsy
    implicit val em: ArticleError[F] = new ArticleError[F] {
      override def pure[A](x: A): F[A] = x.asRight
      override def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] = fa.flatMap(f)
      override def tailRecM[A, B](a: A)(f: A => F[Either[A, B]]): F[B] = ???
      override def raiseError[A](e: Model.ArticleNotFound): F[A] = e.raiseError
      override def handleErrorWith[A](fa: F[A])(f: Model.ArticleNotFound => F[A]): F[A] = ???
    }

    // TODO - Adam suggested this might work, but it doesn't... :(
    //        Might need to look into how MonadError construction based on either can be made nicer
    // import cats.instances.either._
    // implicit val em2: ArticleError[F] = MonadError[F, ArticleNotFound]
  }

  it should "(MonadThrow) process article correctly when present in the database" in new Fixture {
    ProcessArticle.mkWithMonadThrow[F].process(articleId) shouldEqual expected.asRight
    ProcessArticle.mkWithMonadOriginal[F].process(articleId) shouldEqual expected.asRight
  }

  it should "(MonadError) process article correctly when present in the database" in new FixtureWithMonadError {
    ProcessArticle.mkWithArticleNotFoundError[F].process(articleId) shouldEqual expected.asRight
    ProcessArticle.mkWithArticleNotFoundError2[F].process(articleId) shouldEqual expected.asRight
    ProcessArticle.mkWithArticleNotFoundError3[F].process(articleId) shouldEqual expected.asRight
    ProcessArticle.mkWithArticleNotFoundError4[F].process(articleId) shouldEqual expected.asRight
  }
}
