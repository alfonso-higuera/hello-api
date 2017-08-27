package com.crabi.controllers

import javax.inject.{Inject, Singleton}

import com.crabi.models.http._
import com.crabi.services.HelloService
import com.twitter.finatra.http.Controller

@Singleton
class HelloController @Inject()(helloService: HelloService) extends Controller {

  get("/hello/:id/?") { helloGetRequest: HelloGetRequest =>
    helloService.getResponseHello(helloGetRequest)
  }

  post("/hello/?") { helloPostRequest: HelloPostRequest =>
    for {
      savedHello <- helloService.save(helloPostRequest)
      helloResponse = HelloResponse.fromModel(savedHello)
    } yield {
      response
        .created(helloResponse)
        .location(helloResponse.id)
    }
  }

  put("/hello/?") { helloPutRequest: HelloPutRequest =>
    for {
      optionalUpdatedHello <- helloService.update(helloPutRequest)
    } yield {
      for {
        updatedHello <- optionalUpdatedHello
      } yield {
        HelloResponse.fromModel(updatedHello)
      }
    }
  }

  delete("/hello/:id/?") { helloDeleteRequest: HelloDeleteRequest =>
    for {
      optionalDeleteResult <- helloService.delete(helloDeleteRequest)
    } yield {
      for {
        _ <- optionalDeleteResult
      } yield {
        response.noContent
      }
    }
  }
}
