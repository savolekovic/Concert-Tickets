package com.example.concerttickets.modules.concert_tickets.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "concert_ticket")
data class ConcertTicket(
    @PrimaryKey(autoGenerate = true)
    val ticket_id: Int? = null,

    @Embedded var payload: Payload,
    var type: String
)