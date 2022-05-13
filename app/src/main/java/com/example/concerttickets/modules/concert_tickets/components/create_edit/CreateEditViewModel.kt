package com.example.concerttickets.modules.concert_tickets.components.create_edit

import androidx.lifecycle.ViewModel
import com.example.concerttickets.modules.concert_tickets.servicelayer.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateEditViewModel
@Inject constructor(private val mainRepository: MainRepository): ViewModel(){
}