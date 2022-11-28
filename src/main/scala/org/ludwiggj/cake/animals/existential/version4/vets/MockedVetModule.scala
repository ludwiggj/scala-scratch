package org.ludwiggj.cake.animals.existential.version4.vets

import org.mockito.Mockito._
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import scala.language.implicitConversions

// See https://www.cakesolutions.net/teamblogs/2011/12/19/cake-pattern-in-depth
trait MockedVetModule extends VetModule {
  type Vet = VetLike

  val aVet = mock(classOf[VetLike])

  override def Vet(name: String): VetLike = {
    when (aVet.name).thenReturn(name)
    aVet
  }

  def expect(f: (VetLike) => Any): Unit = {
    // As per https://stackoverflow.com/questions/15024352/mockitos-answer-in-scalatest
    implicit def toAnswerWithArgs[T](f: InvocationOnMock => T): Answer[T] =
      (i: InvocationOnMock) => f(i)

    f(aVet)
  }

  def postCheck(f: (VetLike) => Any): Unit = expect(f)
}