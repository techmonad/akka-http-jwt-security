package com.techmonad

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport {

  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val tokenJsonFormat = jsonFormat1(JwtToken)
  implicit val authJsonFormat = jsonFormat1(Auth)
  implicit val userJsonFormat = jsonFormat4(User)
  implicit val usersJsonFormat = jsonFormat1(Users)

}
