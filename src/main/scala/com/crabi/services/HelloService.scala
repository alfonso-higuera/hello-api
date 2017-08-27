package com.crabi.services

import com.crabi.models.Hello
import com.crabi.models.http._
import com.twitter.util.{Closable, Future}

trait HelloService extends Closable {

  def save(helloPostRequest: HelloPostRequest): Future[Hello]

  def update(helloPutRequest: HelloPutRequest): Future[Option[Hello]]

  def getResponseHello(helloGetRequest: HelloGetRequest): Future[Option[HelloResponse]]

  def delete(helloDeleteRequest: HelloDeleteRequest): Future[Option[Unit]]
}
