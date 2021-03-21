package com.techmonad

import java.time.Clock

import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{ AuthorizationFailedRejection, Directive1 }
import akka.http.scaladsl.server.Directives._
import pdi.jwt.{ Jwt, JwtAlgorithm, JwtClaim }
import spray.json._

import scala.util.control.NonFatal
import scala.util.{ Failure, Success }

trait JwtSecurity extends JsonSupport {

  private val expiresIn = 1 * 24 * 60 * 60
  implicit val clock: Clock = Clock.systemUTC

  private val secretKey = "techmonad-akka-http-jwt"

  def encodeToken(user: User): String = {
    val claim =
      JwtClaim(user.toJson.prettyPrint)
        .issuedNow
        .expiresIn(expiresIn)
    Jwt.encode(claim, secretKey, JwtAlgorithm.HS256)
  }

  def authenticatedWithRole(role: String): Directive1[User] = {
    authenticated flatMap {
      case t if t.roles.contains(role) => provide(t)
      case _ => reject(AuthorizationFailedRejection).toDirective[Tuple1[User]]
    }
  }

  def authenticated: Directive1[User] =
    optionalHeaderValueByName("Authorization").flatMap {
      case Some(jwtToken) if Jwt.isValid(jwtToken, secretKey, Seq(JwtAlgorithm.HS256)) =>
        getClaims(jwtToken) match {
          case Some(user) => provide(user)
          case None => reject(AuthorizationFailedRejection).toDirective[Tuple1[User]]
        }

      case t =>
        println(t.get)
        complete(StatusCodes.Unauthorized)
    }

  private def getClaims(jwtToken: String): Option[User] = {
    try {
      Jwt.decode(jwtToken, secretKey, Seq(JwtAlgorithm.HS256)) match {
        case Success(value) =>
          Some(value.content.parseJson.convertTo[User])
        case Failure(ex) =>
          ex.printStackTrace()
          None
      }
    } catch {
      case NonFatal(ex) => None
    }
  }

}
