package com.techmonad

import akka.actor.{ Actor, ActorLogging, Props }

final case class JwtToken(token: String)

final case class Auth(name: String)

final case class User(name: String, age: Int, country: String, roles: List[String])

final case class Users(users: Seq[User])

object UserRegistryActor {

  final case class ActionPerformed(description: String)

  final case object GetUsers

  final case class CreateUser(user: User)

  final case class GetUser(name: String)

  final case class DeleteUser(name: String)

  def props: Props = Props[UserRegistryActor]

}

class UserRegistryActor extends Actor with ActorLogging {

  import UserRegistryActor._

  var users: Set[User] = Set(User("Andy", 31, "France", List("admin", "user")))

  def receive: Receive = {
    case Auth(name) =>
      sender() ! users.find(_.name == name)
    case GetUsers =>
      sender() ! Users(users.toSeq)
    case CreateUser(user) =>
      users += user
      sender() ! ActionPerformed(s"User ${user.name} created.")
    case GetUser(name) =>
      sender() ! users.find(_.name == name)
    case DeleteUser(name) =>
      users.find(_.name == name) foreach { user => users -= user }
      sender() ! ActionPerformed(s"User $name deleted.")
  }

}
