package com.techmonad

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.util.{ Failure, Success }

object QuickStartServer extends App with UserRoutes {

  // set up ActorSystem and other dependencies here
  implicit val system: ActorSystem = ActorSystem("TechmonadAkkaHttpServer")
  implicit val executionContext: ExecutionContext = system.dispatcher

  override val userRegistry = UserRegistry

  val serverBinding: Future[Http.ServerBinding] =
    Http()
      .newServerAt("localhost", 8080)
      .bind(userRoutes)

  serverBinding
    .onComplete {
      case Success(bound) =>
        println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
      case Failure(e) =>
        Console.err.println(s"Server could not start!")
        e.printStackTrace()
        system.terminate()
    }

}
