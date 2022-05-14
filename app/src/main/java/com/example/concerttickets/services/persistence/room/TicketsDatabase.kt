package com.example.concerttickets.services.persistence.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.concerttickets.modules.concert_tickets.models.ConcertTicket

@Database(
    entities = [ConcertTicket::class],
    version = 3
)
abstract class TicketsDatabase : RoomDatabase() {

    abstract fun ticketsDao(): TicketsDao

    companion object {
        val DATABASE_NAME: String = "tickets_dao"
    }
}