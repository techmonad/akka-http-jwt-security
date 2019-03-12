package com.techmonad

final case class JwtToken(token: String)

final case class Auth(name: String)

final case class User(name: String, age: Int, country: String, roles: List[String])

final case class Users(users: Seq[User])

trait UserRegistry {

  var users: Set[User] = Set(User("Andy", 31, "France", List("admin", "user")))

  def getUser(auth: Auth) = {
    users.find(_.name == auth.name)
  }

  def getUser(name: String) = {
    users.find(_.name == name)
  }

  def getUsers(): Users = {
    Users(users.toSeq)
  }

  def create(user: User) = {
    users += user
    s"User ${user.name} created."
  }

  def delete(name: String) = {
    users.find(_.name == name) foreach { user => users -= user }
    s"User $name deleted."
  }

}

object UserRegistry extends UserRegistry