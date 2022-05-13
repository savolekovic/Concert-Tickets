package com.example.concerttickets.modules.concert_tickets.models

import com.squareup.moshi.Json

data class Payload(
    @Json(name="payload_id")
    val id: Int,
    val date: String,
    val description: String,
    val name: String,
    val photo: String,
    val price: Int,
    val quantity: Int,

    val place: String? = null,
    val discount: Int? = null
)