package com.crabi.models.http

import com.twitter.finatra.validation.UUID

case class HelloPutRequest(@UUID id: String, name: String)
