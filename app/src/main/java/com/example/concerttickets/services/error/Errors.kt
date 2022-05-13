package com.example.concerttickets.services.error

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Errors(
    @Json(name = "email")
    val email: List<String>?
)