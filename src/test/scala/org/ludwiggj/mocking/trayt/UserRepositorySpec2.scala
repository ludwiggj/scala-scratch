package org.ludwiggj.mocking.trayt

import org.ludwiggj.mocking.User
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

// Mix in DB directly
// Similar to UserDBForTesting but in this case, we don’t need an extra class,
// and the implementation is exactly what our test needs and nothing else.
class UserRepositorySpec2 extends AnyFlatSpec with Matchers with DB[User, String] {
  // Potentially need to implement everything here
  val users = Seq("Joe", "Nicolas", "Ruth", "Doe", "Maria")
  override def all(): Seq[String] = users
  override def store(a: User): String = ???
  override def retrieve(id: String): Option[User] = if (users.contains(id)) Some(User(id)) else None

  it should "select user with short name take 1" in {
    val usersWithShortName = UserRepository(this).select(_.id.length <= 3)

    usersWithShortName should contain(User("Joe"))
    usersWithShortName should contain(User("Doe"))
  }
}