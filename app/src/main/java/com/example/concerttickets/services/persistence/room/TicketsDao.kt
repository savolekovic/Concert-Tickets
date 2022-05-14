package com.example.concerttickets.services.persistence.room

import androidx.room.*
import com.example.concerttickets.modules.concert_tickets.models.ConcertTicket
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketsDao {

    @Query("SELECT * FROM concert_ticket")
    fun getConcertTickets(): Flow<List<ConcertTicket>>

    @Query("SELECT * FROM concert_ticket WHERE type == :ticketType")
    fun getTicketsByType(ticketType: String): Flow<List<ConcertTicket>>

    @Query("SELECT * FROM concert_ticket WHERE ticket_id == :ticketId")
    fun getDetails(ticketId: Int): Flow<ConcertTicket>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTickets(tickets: List<ConcertTicket>)

    @Query("DELETE FROM concert_ticket")
    suspend fun deleteAllTickets()

    @Delete
    suspend fun deleteConcertTicket(concertTicket: ConcertTicket)

}