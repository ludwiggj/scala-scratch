package org.ludwiggj.taglessfinal.testing.itv.news

import cats.implicits.{catsSyntaxEitherId, catsSyntaxOptionId}
import cats.instances.either._
import org.ludwiggj.taglessfinal.testing.itv.news.Model.{Article, ArticleEvent, ArticleNotFound}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ProcessArticleSpec extends AnyFlatSpec with Matchers {
  trait Fixture[A] {
    type F[B] = Either[A, B]

    val articleId: String = "20230120_001"
    val article: Article = Article(id = articleId, title = "How to test tagless final", topic = "Testing")

    val expected: ArticleEvent = ArticleEvent(article, Nil)

    implicit val log: Log[F] = _ => ().asRight
    implicit val repo: ArticleRepo[F] = _ => article.some.asRight
    implicit val contentfulClient: ContentfulClient[F] = _ => List.empty.asRight
    implicit val eventProducer: ProduceEvent[F] = _ => ().asRight
  }

  behavior of "(MonadThrow) Process Article (article present)"

  it should "take1: Process article correctly" in new Fixture[Throwable] {
    ProcessArticle.UsingMonadThrow.mkTake1[F].process(articleId) shouldEqual expected.asRight
  }

  it should "take2: Process article correctly" in new Fixture[Throwable] {
    ProcessArticle.UsingMonadThrow.mkTake2[F].process(articleId) shouldEqual expected.asRight
  }

  behavior of "(MonadError) Process Article (article present)"

  it should "take1: Process article correctly" in new Fixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake1[F].process(articleId) shouldEqual expected.asRight
  }

  it should "take2: Process article correctly" in new Fixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake2[F].process(articleId) shouldEqual expected.asRight
  }

  it should "take3: Process article correctly" in new Fixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake3[F].process(articleId) shouldEqual expected.asRight
  }

  it should "take4: Process article correctly" in new Fixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake4[F].process(articleId) shouldEqual expected.asRight
  }

  it should "take5: Process article correctly" in new Fixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake5[F].process(articleId) shouldEqual expected.asRight
  }
}
