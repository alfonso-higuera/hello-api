package com.crabi.services.implementations.cassandra

import java.util.UUID
import javax.inject.Singleton

import com.crabi.models.Hello
import com.crabi.models.http._
import com.crabi.services.HelloService
import com.datastax.driver.core.{Cluster, Row, Session}
import com.google.common.util.concurrent.{FutureCallback, Futures, ListenableFuture}
import com.twitter.util.{Duration, Future, Promise, Time}

@Singleton
object  CassandraHelloService extends HelloService {

  import com.crabi.configuration.Configuration.Cassandra

  private[this] implicit class RichListenableFuture[T](listenableFuture: ListenableFuture[T]) {
    def asTwitterFuture: Future[T] = {
      val promise = Promise[T]()
      Futures.addCallback(listenableFuture, new FutureCallback[T] {

        def onFailure(t: Throwable): Unit = promise.setException(t)

        def onSuccess(result: T): Unit = promise.setValue(result)
      })
      promise
    }
  }

  private[this] val cluster: Cluster =
    Cluster
      .builder()
      .addContactPoint(Cassandra.hostname)
      .build()

  private[this] val session: Session = cluster.connect()

  session.execute(s"USE ${Cassandra.keyspace}")

  override def getResponseHello(helloGetRequest: HelloGetRequest): Future[Option[HelloResponse]] =
    session
      .executeAsync(s"SELECT * FROM hello WHERE hello_id=${helloGetRequest.id}")
      .asTwitterFuture
      .map { resultSet =>
        val row: Row = resultSet.one()
        if (row == null) {
          None
        } else {
          Some(HelloResponse(id = row.getUUID("hello_id").toString, name = row.getString("name")))
        }
      }

  override def save(helloPostRequest: HelloPostRequest): Future[Hello] = {
    val newRandomId: UUID = UUID.randomUUID()
    session
      .executeAsync(
        s"""INSERT INTO hello (hello_id, name)
            VALUES (${newRandomId.toString}, '${helloPostRequest.name}')
            IF NOT EXISTS""")
      .asTwitterFuture
      .map { _ => Hello(id = newRandomId, name = helloPostRequest.name) }
  }

  override def update(helloPutRequest: HelloPutRequest): Future[Option[Hello]] =
    session
      .executeAsync(
        s"""UPDATE hello
            SET name='${helloPutRequest.name}'
            WHERE hello_id=${helloPutRequest.id}
            IF EXISTS""")
      .asTwitterFuture
      .map { resultSet =>
        if (resultSet.wasApplied) {
          Some(Hello(id = UUID.fromString(helloPutRequest.id), name = helloPutRequest.name))
        } else {
          None
        }
      }

  override def delete(helloDeleteRequest: HelloDeleteRequest): Future[Option[Unit]] =
    session
      .executeAsync(s"DELETE FROM hello WHERE hello_id=${helloDeleteRequest.id} IF EXISTS")
      .asTwitterFuture
      .map { resultSet =>
        if (resultSet.wasApplied) {
          Some(())
        } else {
          None
        }
      }

  override def close(deadline: Time): Future[Unit] =
    if (cluster != null) {
      cluster.closeAsync().asTwitterFuture.map { _ => () }
    } else {
      Future.Done
    }
}
