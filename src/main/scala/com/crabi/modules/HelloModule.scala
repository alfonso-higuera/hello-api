package com.crabi.modules

import javax.inject.Singleton

import com.crabi.services.HelloService
import com.crabi.services.implementations.cassandra.CassandraHelloService
import com.google.inject.Provides
import com.twitter.inject.TwitterModule

object HelloModule extends TwitterModule {

  @Singleton
  @Provides
  def providesHelloService: HelloService = CassandraHelloService
}
