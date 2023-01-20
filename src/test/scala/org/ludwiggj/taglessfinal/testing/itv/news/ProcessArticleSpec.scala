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

  trait FixtureWithMondadError extends Fixture {
    implicit val em: ArticleError[F] = new ArticleError[F] {
      override def pure[A](x: A): F[A] = x.asRight
      override def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B] = fa.flatMap(f)
      override def tailRecM[A, B](a: A)(f: A => F[Either[A, B]]): F[B] = ???
      override def raiseError[A](e: Model.ArticleNotFound): F[A] = e.raiseError
      override def handleErrorWith[A](fa: F[A])(f: Model.ArticleNotFound => F[A]): F[A] = ???
    }
  }

  it should "process article correctly when present in the database - mk" in new Fixture {
    ProcessArticle.mk[F].process(articleId) shouldEqual expected.asRight
  }

  it should "process article correctly when present in the database - mk2" in new FixtureWithMondadError {
    ProcessArticle.mkTake2[F].process(articleId) shouldEqual expected.asRight
  }

  it should "process article correctly when present in the database - mkTake3" in new FixtureWithMondadError {
    ProcessArticle.mkTake3[F].process(articleId) shouldEqual expected.asRight
  }
}
