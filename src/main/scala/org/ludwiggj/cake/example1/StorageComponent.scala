package org.ludwiggj.cake.example1

trait StorageComponent {
  type User <: UserLike

  def storeUser(user: User): Unit

  def retrieveUser(id: Int): Option[User]

  trait UserLike {
    def id: Int

    def hash: Vector[Byte]
  }

}