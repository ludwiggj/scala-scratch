package org.ludwiggj.cake.example2

trait StorageComponent {
  type User <: UserLike

  def storeUser(user: User): Unit

  def retrieveUser(id: Int): Option[User]

  trait UserLike {
    def id: Int

    def hash: Vector[Byte]
  }

}