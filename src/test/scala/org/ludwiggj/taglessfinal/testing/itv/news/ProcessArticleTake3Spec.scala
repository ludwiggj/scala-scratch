package org.ludwiggj.taglessfinal.testing.itv.news

import cats.data.{EitherT, Writer, WriterT}
import cats.implicits.{catsSyntaxEitherId, catsSyntaxOptionId, none, toFunctorOps}
import cats.instances.either._
import org.ludwiggj.taglessfinal.testing.itv.news.Event.{ArticleFetched, ContentfulCalled, EventProduced}
import org.ludwiggj.taglessfinal.testing.itv.news.Model.{Article, ArticleEvent, ArticleNotFound}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

// TODO At 43:38
class ProcessArticleTake3Spec extends AnyFlatSpec with Matchers {
  val articleId: String = "20230120_001"
  val article: Article = Article(id = articleId, title = "How to test tagless final", topic = "Testing")
  val expectedArticleEvent: ArticleEvent = ArticleEvent(article, Nil)

  trait HappyPathWithoutErrorEventsFixture[E] {
    type Error[A] = Either[E, A]
    type F[A] = WriterT[Error, List[Event], A] // Can log a List of Events, and handle errors

    val expectedEvents: List[Event] = List(
      ArticleFetched(articleId), ContentfulCalled(article.topic), EventProduced(expectedArticleEvent)
    )

    implicit val log: Log[F] = _ =>
      WriterT.value(())

    implicit val repo: ArticleRepo[F] = id =>
      WriterT.tell[Error, List[Event]](List(ArticleFetched(id))).as(article.some)

    implicit val contentfulClient: ContentfulClient[F] = topic =>
      WriterT.tell[Error, List[Event]](List(ContentfulCalled(topic))).as(Nil)

    implicit val eventProducer: ProduceEvent[F] = articleEvent =>
      WriterT.tell[Error, List[Event]](List(EventProduced(articleEvent))).as(())
  }

  behavior of "(MonadThrow) Process Article (article present) (without error events)"

  it should "take1: Return article" in new HappyPathWithoutErrorEventsFixture[Throwable] {
    val result: Error[(List[Event], ArticleEvent)] = ProcessArticle.UsingMonadThrow.mkTake1[F].process(articleId).run
    val result2: Either[Throwable, (List[Event], ArticleEvent)] = result

    result2 shouldEqual (expectedEvents, expectedArticleEvent).asRight
  }

  it should "take2: Return article" in new HappyPathWithoutErrorEventsFixture[Throwable] {
    ProcessArticle.UsingMonadThrow.mkTake2[F].process(articleId).run shouldEqual (expectedEvents, expectedArticleEvent).asRight
  }

  behavior of "(MonadError) Process Article (article present) (without error events)"

  it should "take1: Return article" in new HappyPathWithoutErrorEventsFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake1[F].process(articleId).run shouldEqual (expectedEvents, expectedArticleEvent).asRight
  }

  it should "take2: Return article" in new HappyPathWithoutErrorEventsFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake2[F].process(articleId).run shouldEqual (expectedEvents, expectedArticleEvent).asRight
  }

  it should "take3: Return article" in new HappyPathWithoutErrorEventsFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake3[F].process(articleId).run shouldEqual (expectedEvents, expectedArticleEvent).asRight
  }

  it should "take4: Return article" in new HappyPathWithoutErrorEventsFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake4[F].process(articleId).run shouldEqual (expectedEvents, expectedArticleEvent).asRight
  }

  it should "take5: Return article" in new HappyPathWithoutErrorEventsFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake5[F].process(articleId).run shouldEqual (expectedEvents, expectedArticleEvent).asRight
  }

  it should "take6: Return article" in new HappyPathWithoutErrorEventsFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake6[F].process(articleId).run shouldEqual (expectedEvents, expectedArticleEvent).asRight
  }

  trait NoArticleInDatabaseWithoutErrorEventsFixture[E] {
    type Error[A] = Either[E, A]
    type F[A] = WriterT[Error, List[Event], A] // Can log a List of Events, and handle errors

    val expectedEvents: List[Event] = List(ArticleFetched(articleId))

    implicit val log: Log[F] = _ =>
      WriterT.value(())

    implicit val repo: ArticleRepo[F] = id =>
      WriterT.tell[Error, List[Event]](List(ArticleFetched(id))).as(none)

    //noinspection NotImplementedCode
    implicit val contentfulClient: ContentfulClient[F] = _ => ???

    //noinspection NotImplementedCode
    implicit val eventProducer: ProduceEvent[F] = _ => ???
  }

  behavior of "(MonadThrow) Process Article (article missing) (without error events)"

  it should "take1: No article found exception" in new NoArticleInDatabaseWithoutErrorEventsFixture[Throwable] {
    val result: Error[(List[Event], ArticleEvent)] = ProcessArticle.UsingMonadThrow.mkTake1[F].process(articleId).run
    val result2: Either[Throwable, (List[Event], ArticleEvent)] = result

    // Can't verify the events, as they only appear on the right of the either
    result2 shouldEqual ArticleNotFound(articleId).asLeft
  }

  trait NoArticleInDatabaseFixture[E] {
    type EventWriter[A] = Writer[List[Event], A]
    type F[A] = EitherT[EventWriter, E, A]

    val expectedEvents: List[Event] = List(ArticleFetched(articleId))

    implicit val log: Log[F] = _ =>
      EitherT.pure(())

    // Expected return type is:
    //    F[Option[Article]]
    // => EitherT[EventWriter, E, Option[Article]]
    implicit val repo: ArticleRepo[F] = id => {
      val writer: EventWriter[Option[Article]] = Writer.tell(List[Event](ArticleFetched(id))).as(none)
      val result: EitherT[EventWriter, E, Option[Article]] = EitherT.liftF(writer)
      result
    }

    //noinspection NotImplementedCode
    implicit val contentfulClient: ContentfulClient[F] = _ => ???

    //noinspection NotImplementedCode
    implicit val eventProducer: ProduceEvent[F] = _ => ???
  }

  behavior of "(MonadThrow) Process Article (article missing)"

  it should "take1: No article found exception" in new NoArticleInDatabaseFixture[Throwable] {
    val result: (List[Event], Either[Throwable, ArticleEvent]) = ProcessArticle.UsingMonadThrow.mkTake1[F].process(articleId).value.run

    result shouldEqual(expectedEvents, ArticleNotFound(articleId).asLeft)
  }

  it should "take2: No article found exception" in new NoArticleInDatabaseFixture[Throwable] {
    ProcessArticle.UsingMonadThrow.mkTake2[F].process(articleId).value.run shouldEqual(expectedEvents, ArticleNotFound(articleId).asLeft)
  }

  behavior of "(MonadError) Process Article (article missing)"

  it should "take1: No article found exception" in new NoArticleInDatabaseFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake1[F].process(articleId).value.run shouldEqual(expectedEvents, ArticleNotFound(articleId).asLeft)
  }

  it should "take2: No article found exception" in new NoArticleInDatabaseFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake2[F].process(articleId).value.run shouldEqual(expectedEvents, ArticleNotFound(articleId).asLeft)
  }

  it should "take3: No article found exception" in new NoArticleInDatabaseFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake3[F].process(articleId).value.run shouldEqual(expectedEvents, ArticleNotFound(articleId).asLeft)
  }

  it should "take4: No article found exception" in new NoArticleInDatabaseFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake4[F].process(articleId).value.run shouldEqual(expectedEvents, ArticleNotFound(articleId).asLeft)
  }

  it should "take5: No article found exception" in new NoArticleInDatabaseFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake5[F].process(articleId).value.run shouldEqual(expectedEvents, ArticleNotFound(articleId).asLeft)
  }

  it should "take6: No article found exception" in new NoArticleInDatabaseFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake6[F].process(articleId).value.run shouldEqual(expectedEvents, ArticleNotFound(articleId).asLeft)
  }

  trait HappyPathFixture[E] {
    type EventWriter[A] = Writer[List[Event], A]
    type F[A] = EitherT[EventWriter, E, A]

    val expectedEvents: List[Event] = List(
      ArticleFetched(articleId), ContentfulCalled(article.topic), EventProduced(expectedArticleEvent)
    )

    // https://github.com/dantb/unit-testing-either-writer/tree/main
    // https://itv.slack.com/archives/G7XFWU9HA/p1668781379559659
    // https://typelevel.org/cats/nomenclature.html
    // https://www.stackage.org/lts-20.11/hoogle?q=%3D%3E%3E
    // https://blog.ssanj.net/posts/2017-07-02-working-with-arrows-in-scala.html

    def logEventAndValue[A](l: Event, a: A): EitherT[EventWriter, E, A] =
    // def logEventAndValue[A](l: Event, a: A): EitherT[[X] =>> Writer[List[Event], X], E, A] =
      EitherT.liftF(Writer.tell(List(l)).as(a))

    implicit val log: Log[F] = _ =>
      EitherT.pure(())

    implicit val repo: ArticleRepo[F] = id =>
      logEventAndValue(ArticleFetched(id), article.some)

    //noinspection NotImplementedCode
    implicit val contentfulClient: ContentfulClient[F] = topic =>
      logEventAndValue(ContentfulCalled(topic), Nil)

    //noinspection NotImplementedCode
    implicit val eventProducer: ProduceEvent[F] = articleEvent =>
      logEventAndValue(EventProduced(articleEvent), ())
  }

  behavior of "(MonadThrow) Process Article (article present)"

  it should "take1: Return article" in new HappyPathFixture[Throwable] {
    ProcessArticle.UsingMonadThrow.mkTake1[F].process(articleId).value.run shouldEqual(expectedEvents, expectedArticleEvent.asRight)
  }

  it should "take2: Return article" in new HappyPathFixture[Throwable] {
    ProcessArticle.UsingMonadThrow.mkTake2[F].process(articleId).value.run shouldEqual(expectedEvents, expectedArticleEvent.asRight)
  }

  behavior of "(MonadError) Process Article (article present)"

  it should "take1: Return article" in new HappyPathFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake1[F].process(articleId).value.run shouldEqual(expectedEvents, expectedArticleEvent.asRight)
  }

  it should "take2: Return article" in new HappyPathFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake2[F].process(articleId).value.run shouldEqual(expectedEvents, expectedArticleEvent.asRight)
  }

  it should "take3: Return article" in new HappyPathFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake3[F].process(articleId).value.run shouldEqual(expectedEvents, expectedArticleEvent.asRight)
  }

  it should "take4: Return article" in new HappyPathFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake4[F].process(articleId).value.run shouldEqual(expectedEvents, expectedArticleEvent.asRight)
  }

  it should "take5: Return article" in new HappyPathFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake5[F].process(articleId).value.run shouldEqual(expectedEvents, expectedArticleEvent.asRight)
  }

  it should "take6: Return article" in new HappyPathFixture[ArticleNotFound] {
    ProcessArticle.UsingMonadError.mkTake6[F].process(articleId).value.run shouldEqual(expectedEvents, expectedArticleEvent.asRight)
  }
}
