package com.crabi.models.http

import com.twitter.finatra.request.RouteParam
import com.twitter.finatra.validation.UUID

case class HelloGetRequest(@UUID @RouteParam id: String)
