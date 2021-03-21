package com.techmonad

import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{ Authorization, OAuth2BearerToken, RawHeader }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class UserRoutesSpec extends AnyWordSpec with Matchers with ScalatestRouteTest with UserRoutes {

  lazy val routes = userRoutes
  override val userRegistry = UserRegistry
  val user = userRegistry.users.head
  val token = encodeToken(user)

  "UserRoutes" should {
    "return  users(GET /users)" in {
      val request = HttpRequest(uri = "/users")
        .addHeader(RawHeader("Authorization", token))
      request ~> routes ~> check {
        status should ===(StatusCodes.OK)
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("""{"users":[{"age":31,"country":"France","name":"Andy","roles":["admin","user"]}]}""")
      }
    }

    "be able to add users (POST /users)" in {
      val user = User("Kapi", 42, "jp", List("admin", "user"))
      val userEntity = Marshal(user).to[MessageEntity].value.get.get
      val request = Post("/users")
        .withEntity(userEntity)
        .addHeader(RawHeader("Authorization", token))
      request ~> routes ~> check {
        status should ===(StatusCodes.OK)
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("User Kapi created.")
      }
    }

    "be able to get user by name" in {
      val request = Get(uri = "/users/Kapi")
        .addHeader(RawHeader("Authorization", token))

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("""{"age":42,"country":"jp","name":"Kapi","roles":["admin","user"]}""")
      }
    }

    "be able to remove users (DELETE /users)" in {
      val request = Delete(uri = "/users/Kapi")
        .addHeader(RawHeader("Authorization", token))

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)
        contentType should ===(ContentTypes.`application/json`)
        entityAs[String] should ===("User Kapi deleted.")
      }
    }

  }

}
