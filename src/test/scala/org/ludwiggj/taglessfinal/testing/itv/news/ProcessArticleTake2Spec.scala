package org.ludwiggj.taglessfinal.testing.itv.news

import cats.data.WriterT
import cats.implicits.{catsSyntaxEitherId, catsSyntaxOptionId, toFunctorOps}
import cats.instances.either._
import org.ludwiggj.taglessfinal.testing.itv.news.Model.{Article, ArticleEvent, ArticleNotFound}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

// Immutable, pure functional approach
class ProcessArticleTake2Spec extends AnyFlatSpec with Matchers {

  trait Fixture[A] {
    type Error[B] = Either[A, B]
    type F[B] = WriterT[Error, List[Event], B] // Can log a List of Events, and handle errors

    val articleId: String = "20230120_001"
    val article: Article = Article(id = articleId, title = "How to test tagless final", topic = "Testing")

    val expectedArticleEvent: ArticleEvent = ArticleEvent(article, Nil)

    val expectedEvents: List[Event] = List(
      Event.ArticleFetched(articleId), Event.ContentfulCalled(article.topic), Event.EventProduced(expectedArticleEvent)
    )

    implicit val log: Log[F] = _ =>
      WriterT.tell[Error, List[Event]](Nil).as(())

    implicit val repo: ArticleRepo[F] = id =>
      WriterT.tell[Error, List[Event]](List(Event.ArticleFetched(id))).as(article.some)

    implicit val contentfulClient: ContentfulClient[F] = topic =>
      WriterT.tell[Error, List[Event]](List(Event.ContentfulCalled(topic))).as(Nil)

    implicit val eventProducer: ProduceEvent[F] = articleEvent =>
      WriterT.tell[Error, List[Event]](List(Event.EventProduced(articleEvent))).as(())
  }

  behavior of "(MonadThrow) Process Article (article present)"

  it should "take1: Process article correctly" in new Fixture[Throwable] {
    val result: Error[(List[Event], ArticleEvent)] = ProcessArticle.UsingMonadThrow.mkTake1[F].process(articleId).run
    val result2: Either[Throwable, (List[Event], ArticleEvent)] = result

    result2 shouldEqual (expectedEvents, expectedArticleEvent).asRight
  }

  it should "take2: Process article correctly" in new Fixture[Throwable] {
    ProcessArticle.UsingMonadThrow.mkTake2[F].process(articleId).run shouldEqual (expectedEvents, expectedArticleEvent).asRight
  }

  behavior of "(MonadError) Process Article (article present)"

  it should "take1: Process article correctly" in new Fixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake1[F].process(articleId).run shouldEqual (expectedEvents, expectedArticleEvent).asRight
  }

  it should "take2: Process article correctly" in new Fixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake2[F].process(articleId).run shouldEqual (expectedEvents, expectedArticleEvent).asRight
  }

  it should "take3: Process article correctly" in new Fixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake3[F].process(articleId).run shouldEqual (expectedEvents, expectedArticleEvent).asRight
  }

  it should "take4: Process article correctly" in new Fixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake4[F].process(articleId).run shouldEqual (expectedEvents, expectedArticleEvent).asRight
  }

  it should "take5: Process article correctly" in new Fixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake5[F].process(articleId).run shouldEqual (expectedEvents, expectedArticleEvent).asRight
  }
}
