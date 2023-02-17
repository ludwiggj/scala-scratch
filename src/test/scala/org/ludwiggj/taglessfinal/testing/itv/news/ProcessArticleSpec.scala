package org.ludwiggj.taglessfinal.testing.itv.news

import cats.implicits.{catsSyntaxEitherId, catsSyntaxOptionId}
import cats.instances.either._
import org.ludwiggj.taglessfinal.testing.itv.news.Model.{Article, ArticleEvent, ArticleNotFound}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable.ListBuffer

class ProcessArticleSpec extends AnyFlatSpec with Matchers {

  trait Fixture[E] {
    type F[A] = Either[E, A]

    val articleId: String = "20230120_001"
    val article: Article = Article(id = articleId, title = "How to test tagless final", topic = "Testing")

    val expectedArticleEvent: ArticleEvent = ArticleEvent(article, Nil)

    val expectedEvents: List[Event] = List(
      Event.ArticleFetched(articleId), Event.ContentfulCalled(article.topic), Event.EventProduced(expectedArticleEvent)
    )

    val actualEvents: ListBuffer[Event] = ListBuffer.empty

    implicit val log: Log[F] = _ => ().asRight

    implicit val repo: ArticleRepo[F] = id => {
      actualEvents.append(Event.ArticleFetched(id))
      article.some.asRight
    }

    implicit val contentfulClient: ContentfulClient[F] = topic => {
      actualEvents.append(Event.ContentfulCalled(topic))
      List.empty.asRight
    }

    implicit val eventProducer: ProduceEvent[F] = articleEvent => {
      actualEvents.append(Event.EventProduced(articleEvent))
      ().asRight
    }
  }

  behavior of "(MonadThrow) Process Article (article present)"

  it should "take1: Process article correctly" in new Fixture[Throwable] {
    ProcessArticle.UsingMonadThrow.mkTake1[F].process(articleId) shouldEqual expectedArticleEvent.asRight
    actualEvents shouldEqual expectedEvents
  }

  it should "take2: Process article correctly" in new Fixture[Throwable] {
    ProcessArticle.UsingMonadThrow.mkTake2[F].process(articleId) shouldEqual expectedArticleEvent.asRight
    actualEvents shouldEqual expectedEvents
  }

  behavior of "(MonadError) Process Article (article present)"

  it should "take1: Process article correctly" in new Fixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake1[F].process(articleId) shouldEqual expectedArticleEvent.asRight
    actualEvents shouldEqual expectedEvents
  }

  it should "take2: Process article correctly" in new Fixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake2[F].process(articleId) shouldEqual expectedArticleEvent.asRight
    actualEvents shouldEqual expectedEvents
  }

  it should "take3: Process article correctly" in new Fixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake3[F].process(articleId) shouldEqual expectedArticleEvent.asRight
    actualEvents shouldEqual expectedEvents
  }

  it should "take4: Process article correctly" in new Fixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake4[F].process(articleId) shouldEqual expectedArticleEvent.asRight
    actualEvents shouldEqual expectedEvents
  }

  it should "take5: Process article correctly" in new Fixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake5[F].process(articleId) shouldEqual expectedArticleEvent.asRight
    actualEvents shouldEqual expectedEvents
  }

  it should "take6: Process article correctly" in new Fixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake6[F].process(articleId) shouldEqual expectedArticleEvent.asRight
    actualEvents shouldEqual expectedEvents
  }
}
