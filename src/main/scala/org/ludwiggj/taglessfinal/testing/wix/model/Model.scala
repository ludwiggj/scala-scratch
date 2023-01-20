package org.ludwiggj.taglessfinal.testing.wix.model

object Model {
  case class UserId(id: String)

  case class OrderId(id: String)

  case class UserProfile(userId: UserId, userName: String)

  case class Order(userId: UserId, orderId: OrderId)

  case class UserInformation(userName: String, orders: List[Order])

  object UserInformation {
    def from(profile: UserProfile, orders: List[Order]): UserInformation =
      UserInformation(profile.userName, orders)
  }
}
