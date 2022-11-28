package org.ludwiggj.cake.animals.existential.version3

import org.mockito.Mockito._

trait MockVet extends Vet {
  val mockVet: Vet

  override def vaccinate(pet: Pet): Unit = mockVet.vaccinate(pet.asInstanceOf[mockVet.Pet])
}

object RunPetStores {

  def main(args: Array[String]): Unit = {

    object MockPetStore extends PetStore with MockVet {
      val mockVet = mock(classOf[Vet])

      case class APet(val name: String) extends PetLike

      override type Pet = APet
    }

    // This fails as expected
    val bob = MockPetStore.APet("bob")
    MockPetStore.vaccinate(bob)
    verifyNoInteractions(MockPetStore.mockVet)

    // BUT it gets messy as soon as we want to verify what is passed into vaccinate
    //     Problem is getting the pet types to line up

    //    val steve: MockPetStore.APet = MockPetStore.APet("steve")
    //    val steve: MockPetStore.Pet = MockPetStore.APet("steve")

    //    MockPetStore.vaccinate(steve)

    //    val zzz = MockPetStore.asInstanceOf[Vet]
    //    verify(MockPetStore.mockVet).vaccinate(steve)
    //    val pet: MockPetStore.mockVet.Pet = steve.asInstanceOf[MockPetStore.mockVet.Pet]
    //    val pet = steve.asInstanceOf[zzz.Pet]
    //    verify(MockPetStore.mockVet).vaccinate(pet)
  }
}