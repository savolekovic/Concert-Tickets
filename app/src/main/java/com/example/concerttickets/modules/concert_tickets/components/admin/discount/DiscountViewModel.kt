package com.example.concerttickets.modules.concert_tickets.components.admin.discount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.concerttickets.modules.concert_tickets.models.ConcertTicket
import com.example.concerttickets.modules.concert_tickets.servicelayer.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscountViewModel
@Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    fun getDiscountedTickets(ticketType: String) =
        mainRepository.getTicketsByType(ticketType).asLiveData()

    fun deleteConcertTicket(concertTicket: ConcertTicket) = viewModelScope.launch {
        mainRepository.deleteConcertTicket(concertTicket)
    }

}