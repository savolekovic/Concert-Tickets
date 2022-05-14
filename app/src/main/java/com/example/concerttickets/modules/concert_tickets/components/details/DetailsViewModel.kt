package com.example.concerttickets.modules.concert_tickets.components.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.concerttickets.modules.concert_tickets.servicelayer.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel
@Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    fun getDetails(ticketId: Int) = mainRepository.getDetails(ticketId).asLiveData()
}