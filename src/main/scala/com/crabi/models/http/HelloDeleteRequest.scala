package com.crabi.models.http

import com.twitter.finatra.request.RouteParam
import com.twitter.finatra.validation.UUID

case class HelloDeleteRequest(@UUID @RouteParam id: String)
