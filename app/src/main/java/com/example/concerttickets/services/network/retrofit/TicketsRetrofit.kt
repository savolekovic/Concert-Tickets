package com.example.concerttickets.services.network.retrofit

import com.example.concerttickets.modules.concert_tickets.models.ConcertTicket
import com.example.concerttickets.utils.GET_CONCERT_TICKETS
import retrofit2.http.GET

interface TicketsRetrofit {

    @GET(GET_CONCERT_TICKETS)
    suspend fun getConcertTickets(): List<ConcertTicket>
}