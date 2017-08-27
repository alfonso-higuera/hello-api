package com.crabi.configuration

import com.typesafe.config.{Config, ConfigFactory}

object Configuration {

  private[this] val application: Config =
    ConfigFactory
      .load()
      .getConfig("application")

  object Cassandra {

    private[this] val cassandra: Config = application.getConfig("cassandra")
    val hostname: String = cassandra.getString("hostname")
    val keyspace: String = cassandra.getString("keyspace")
  }
}
