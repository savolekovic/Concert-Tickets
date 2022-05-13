package com.example.concerttickets.services.error

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "message")
    val message: String?,
    @Json(name = "error_code")
    val errorCode: String?,
    @Json(name = "status_code")
    val statusCode: Int?,
    @Json(name = "errors")
    val errors: Errors?
)