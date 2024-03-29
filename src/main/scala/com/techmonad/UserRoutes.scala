package com.techmonad

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, HttpResponse }
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.{ delete, get, post }
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete

trait UserRoutes extends JwtSecurity {

  implicit def system: ActorSystem
  val userRegistry: UserRegistry

  lazy val userRoutes: Route =
    pathPrefix("auth") {
      post {
        entity(as[Auth]) { auth =>
          val maybeUser: Option[User] = userRegistry.getUser(auth)
          val maybeToken = maybeUser match {
            case Some(user) => Some(JwtToken(encodeToken(user)))
            case None => None
          }
          rejectEmptyResponse {
            complete(maybeToken)
          }
        }
      }

    } ~
      pathPrefix("users") {
        pathEnd {
          get {
            authenticatedWithRole("user") { user =>
              val users = userRegistry.getUsers()
              complete(users)
            }
          } ~
            post {
              authenticatedWithRole("admin") { user =>
                entity(as[User]) { user =>
                  val userCreated = userRegistry.create(user)
                  execute(userCreated)
                }

              }
            }
        } ~
          path(Segment) { name =>
            get {
              authenticatedWithRole("user") { user =>
                val maybeUser: Option[User] = userRegistry.getUser(name)
                rejectEmptyResponse {
                  complete(maybeUser)
                }
              }
            } ~
              delete {
                authenticatedWithRole("admin") { user =>
                  val userDeleted = userRegistry.delete(name)
                  execute(userDeleted)
                }
              }
          }
      }

  def execute(res: String): Route = {
    complete(HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, res)))
  }

}
