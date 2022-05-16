package com.example.concerttickets.modules.concert_tickets.models

import com.squareup.moshi.Json

data class Payload(
    @Json(name = "payload_id")
    val id: Int,
    var date: String,
    var name: String,
    var photo: String,
    var price: Int,
    var quantity: Int,

    var description: String? = null,
    var place: String? = null,
    var discount: Int? = null
)