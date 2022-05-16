package com.example.concerttickets.modules.concert_tickets.components.create_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.concerttickets.R
import com.example.concerttickets.modules.concert_tickets.models.ConcertTicket
import com.example.concerttickets.modules.concert_tickets.servicelayer.MainRepository
import com.example.concerttickets.utils.DISCOUNT
import com.example.concerttickets.utils.EVENT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEditViewModel
@Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    fun getDetails(ticketId: Int) = mainRepository.getDetails(ticketId).asLiveData()

    private val _eventFlow = MutableSharedFlow<Long>()
    val eventFlow = _eventFlow.asSharedFlow()

    val nameErrorFlow = MutableSharedFlow<Int?>()
    val placeErrorFlow = MutableSharedFlow<Int?>()
    val dateErrorFlow = MutableSharedFlow<Int?>()
    val priceErrorFlow = MutableSharedFlow<Int?>()
    val quantityErrorFlow = MutableSharedFlow<Int?>()
    val discountValueErrorFlow = MutableSharedFlow<Int?>()
    //val discountQuantityErrorFlow = MutableSharedFlow<Int?>()

    fun upsert(
        name: String,
        description: String,
        place: String,
        date: String,
        price: String,
        quantity: String,
        discountValue: String,
        isDiscountChecked: Boolean,
        thisTicket: ConcertTicket
    ) {
        viewModelScope.launch {
            if (validateForm(
                    name,
                    place,
                    date,
                    price,
                    quantity,
                    discountValue,
                    isDiscountChecked
                )
            ) {
                thisTicket.payload.name = name
                thisTicket.payload.description = description
                thisTicket.payload.place = place
                thisTicket.payload.date = date
                thisTicket.payload.price = price.toInt()
                thisTicket.payload.quantity = quantity.toInt()

                if (isDiscountChecked) {
                    if (thisTicket.type == EVENT)
                        thisTicket.type = DISCOUNT
                    thisTicket.payload.discount = discountValue.toInt()
                } else {
                    if (thisTicket.type == DISCOUNT)
                        thisTicket.type = EVENT
                }
                _eventFlow.emit(mainRepository.upsert(thisTicket))
            }
        }
    }

    private suspend fun validateForm(
        name: String,
        place: String,
        date: String,
        price: String,
        quantity: String,
        discountValue: String,
        isDiscountChecked: Boolean
    ): Boolean {
        var isValid = true

        //Name
        nameErrorFlow.emit(
            when {
                name.isBlank() -> {
                    isValid = false
                    R.string.alert_empty_name
                }
                else -> null
            }
        )

        //Place
        placeErrorFlow.emit(
            when {
                place.isBlank() -> {
                    isValid = false
                    R.string.alert_empty_place
                }
                else -> null
            }
        )

        //Date
        dateErrorFlow.emit(
            when {
                date.isBlank() -> {
                    isValid = false
                    R.string.alert_empty_date
                }
                else -> null
            }
        )

        //Price
        priceErrorFlow.emit(
            when {
                price.isBlank() -> {
                    isValid = false
                    R.string.alert_empty_price
                }
                else -> null
            }
        )

        //Quantity
        quantityErrorFlow.emit(
            when {
                quantity.isBlank() -> {
                    isValid = false
                    R.string.alert_empty_quantity
                }
                else -> null
            }
        )
        if (isDiscountChecked) {
            //DiscountValue
            discountValueErrorFlow.emit(
                when {
                    discountValue.isBlank() -> {
                        isValid = false
                        R.string.alert_empty_discount_value
                    }
                    discountValue.toInt() > 99 -> {
                        isValid = false
                        R.string.alert_discount_value
                    }
                    else -> null
                }
            )

//            //DiscountQuantity
//            discountQuantityErrorFlow.emit(
//                when {
//                    discountQuantity.isBlank() -> {
//                        isValid = false
//                        R.string.alert_empty_discount_quantity
//                    }
//                    else -> null
//                }
//            )
        }

        return isValid
    }

}