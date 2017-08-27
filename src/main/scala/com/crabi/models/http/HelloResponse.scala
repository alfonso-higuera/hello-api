package com.crabi.models.http

import com.crabi.models.Hello
import com.twitter.finatra.validation.UUID

case class HelloResponse(@UUID id: String, name: String)

object HelloResponse {

  def fromModel(hello: Hello): HelloResponse =
    HelloResponse(id = hello.id.toString, name = hello.name)
}
