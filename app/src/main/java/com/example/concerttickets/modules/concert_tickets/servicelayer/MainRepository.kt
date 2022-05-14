package com.example.concerttickets.modules.concert_tickets.servicelayer


import com.example.concerttickets.modules.concert_tickets.models.ConcertTicket
import com.example.concerttickets.services.network.retrofit.TicketsRetrofit
import com.example.concerttickets.services.persistence.room.TicketsDao
import com.example.concerttickets.utils.Resource
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository
@Inject constructor(
    private val ticketsDao: TicketsDao,
    private val ticketsRetrofit: TicketsRetrofit,
) {

    suspend fun resetDatabase() {
        ticketsDao.deleteAllTickets()
        try {
            ticketsDao.insertAllTickets(ticketsRetrofit.getConcertTickets())
            ticketsDao.getConcertTickets().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            ticketsDao.getConcertTickets().map { Resource.Error(throwable, it) }
        }
    }

    fun getConcertTickets() = flow {
        //If Room db is empty, call api request
        val query = ticketsDao.getConcertTickets().first()
        val shouldFetch = query.isEmpty()

        val flow = if (shouldFetch) {
            emit(Resource.Loading(query))
            try {
                ticketsDao.insertAllTickets(ticketsRetrofit.getConcertTickets())
                ticketsDao.getConcertTickets().map { Resource.Success(it) }
            } catch (throwable: Throwable) {
                ticketsDao.getConcertTickets().map { Resource.Error(throwable, it) }
            }
        } else {
            ticketsDao.getConcertTickets().map { Resource.Success(it) }
        }
        emitAll(flow)
    }

    fun getDiscountedTickets(ticketType: String) = ticketsDao.getTicketsByType(ticketType)

    suspend fun deleteConcertTicket(concertTicket: ConcertTicket) =
        ticketsDao.deleteConcertTicket(concertTicket)

    fun getDetails(ticketId: Int) = ticketsDao.getDetails(ticketId)


}