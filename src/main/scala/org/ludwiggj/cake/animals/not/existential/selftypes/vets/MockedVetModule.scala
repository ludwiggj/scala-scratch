package org.ludwiggj.cake.animals.not.existential.selftypes.vets

import org.ludwiggj.cake.animals.not.existential.selftypes.pets.Pet
import org.mockito.Mockito._
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import scala.language.implicitConversions

// See https://www.cakesolutions.net/teamblogs/2011/12/19/cake-pattern-in-depth
trait MockedVetModule extends VetModule {
  val aVet = mock(classOf[VetModule])
  val vetName = "This will be overridden"

  // Need to pass module method call through to mock
  def vaccinate(pet: Pet): String = {
    aVet.vaccinate(pet)
  }

  def expect(f: (VetModule) => Any): Unit = {
    // As per https://stackoverflow.com/questions/15024352/mockitos-answer-in-scalatest
    implicit def toAnswerWithArgs[T](f: InvocationOnMock => T): Answer[T] =
      (i: InvocationOnMock) => f(i)

    f(aVet)
  }

  def postCheck(f: (VetModule) => Any): Unit = expect(f)
}