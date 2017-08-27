package com.crabi

import com.crabi.controllers.HelloController
import com.crabi.modules.HelloModule
import com.crabi.services.HelloService
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.routing.HttpRouter
import com.twitter.inject.TwitterModule

object ServerMain extends Server {

  closeOnExit {
    val helloService: HelloService = HelloModule.providesHelloService
    helloService
  }
}

class Server extends HttpServer {

  override val modules: Seq[TwitterModule] = Seq(HelloModule)

  override def configureHttp(router: HttpRouter): Unit =
    router
      .filter[CommonFilters]
      .add[HelloController]
}
