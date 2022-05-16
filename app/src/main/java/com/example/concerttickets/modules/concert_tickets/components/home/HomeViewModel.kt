package com.example.concerttickets.modules.concert_tickets.components.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.concerttickets.modules.concert_tickets.servicelayer.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(mainRepository: MainRepository) : ViewModel() {

    val tickets = mainRepository.getConcertTickets().asLiveData()

}