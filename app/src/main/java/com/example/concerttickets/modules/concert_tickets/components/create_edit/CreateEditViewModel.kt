package com.example.concerttickets.modules.concert_tickets.components.create_edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.concerttickets.R
import com.example.concerttickets.modules.concert_tickets.models.ConcertTicket
import com.example.concerttickets.modules.concert_tickets.models.Payload
import com.example.concerttickets.modules.concert_tickets.servicelayer.MainRepository
import com.example.concerttickets.utils.DISCOUNT
import com.example.concerttickets.utils.EVENT
import com.example.concerttickets.utils.TICKET_IMAGE
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
    val discountQuantityErrorFlow = MutableSharedFlow<Int?>()

    fun upsert(
        name: String,
        description: String,
        place: String,
        date: String,
        price: String,
        quantity: String,
        discountValue: String,
        discountQuantity: String,
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
                    discountQuantity,
                    isDiscountChecked
                )
            ) {
                //ID payload-a je uvijek 99, jer mi je baza bila malo zbunjujuca.
                //Nisam znao od pocetka da li je potrebno da kreiram poseban Entity za Payload, ili je bilo dovoljno samo Concert ticket.
                //I previse kasno sam to uocio, pa nisam stigao da pitam.

                val payload = Payload(
                    name = name,
                    description = description,
                    place = place,
                    date = date,
                    price = price.toInt(),
                    quantity = 0,
                    photo = TICKET_IMAGE,
                    id = 99
                )

                if (isDiscountChecked) {
                    if (thisTicket.type == EVENT) {
                        if (quantity.toInt() > discountQuantity.toInt()) {
                            //Update EVENT tickets if the total quantity of tickets is bigger than the Discount quantity
                            payload.quantity = quantity.toInt() - discountQuantity.toInt()
                            thisTicket.payload = payload
                            _eventFlow.emit(mainRepository.upsert(thisTicket))
                            //Create DISCOUNT tickets
                            payload.quantity = discountQuantity.toInt()
                            payload.discount = discountValue.toInt()
                            _eventFlow.emit(
                                mainRepository.upsert(
                                    ConcertTicket(
                                        type = DISCOUNT,
                                        payload = payload
                                    )
                                )
                            )
                        } else {
                            //Update tickets by setting the type to DISCOUNT
                            thisTicket.type = DISCOUNT
                            payload.quantity = discountQuantity.toInt()
                            payload.discount = discountValue.toInt()
                            thisTicket.payload = payload
                            _eventFlow.emit(mainRepository.upsert(thisTicket))
                        }
                    } else {
                        //Update DISCOUNT tickets
                        payload.quantity = discountQuantity.toInt()
                        payload.discount = discountValue.toInt()
                        thisTicket.payload = payload
                        _eventFlow.emit(mainRepository.upsert(thisTicket))
                        if (quantity.toInt() > discountQuantity.toInt()) {
                            //Create EVENT tickets
                            payload.quantity = quantity.toInt() - discountQuantity.toInt()
                            payload.discount = null
                            _eventFlow.emit(
                                mainRepository.upsert(
                                    ConcertTicket(
                                        type = EVENT,
                                        payload = payload
                                    )
                                )
                            )
                        }
                    }
                } else {
                    if (thisTicket.type == DISCOUNT)
                        thisTicket.type = EVENT
                    payload.quantity = quantity.toInt()
                    payload.discount = null
                    thisTicket.payload = payload
                    //Update EVENT tickets
                    _eventFlow.emit(mainRepository.upsert(thisTicket))
                }
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
        discountQuantity: String,
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
                date == "Click to set a date" -> {
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
                price.toInt() <= 0 -> {
                    isValid = false
                    R.string.alert_price_negative
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
                quantity.toInt() <= 0 -> {
                    isValid = false
                    R.string.alert_quantity_negative
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
                    discountValue.toInt() <= 0 -> {
                        isValid = false
                        R.string.alert_discount_value_negative
                    }
                    discountValue.toInt() > 99 -> {
                        isValid = false
                        R.string.alert_discount_value
                    }
                    else -> null
                }
            )

            //DiscountQuantity
            discountQuantityErrorFlow.emit(
                when {
                    discountQuantity.isBlank() -> {
                        isValid = false
                        R.string.alert_empty_discount_quantity
                    }
                    discountQuantity.toInt() <= 0 -> {
                        isValid = false
                        R.string.alert_discount_quantity_negative
                    }
                    discountQuantity.toInt() > quantity.toInt() -> {
                        isValid = false
                        R.string.alert_discount_quantity
                    }
                    else -> null
                }
            )
        }

        return isValid
    }

    fun create(
        name: String,
        description: String,
        place: String,
        date: String,
        price: String,
        quantity: String,
        discountValue: String,
        discountQuantity: String,
        isDiscountChecked: Boolean
    ) = viewModelScope.launch {
        if (validateForm(
                name,
                place,
                date,
                price,
                quantity,
                discountValue,
                discountQuantity,
                isDiscountChecked
            )
        ) {
            if (isDiscountChecked) {
                _eventFlow.emit(
                    mainRepository.upsert(
                        ConcertTicket(
                            type = DISCOUNT,
                            payload = Payload(
                                name = name,
                                date = date,
                                description = description,
                                place = place,
                                price = price.toInt(),
                                quantity = discountQuantity.toInt(),
                                photo = TICKET_IMAGE,
                                discount = discountValue.toInt(),
                                id = 99
                            )
                        )
                    )
                )
                if (quantity.toInt() > discountQuantity.toInt()) {
                    _eventFlow.emit(
                        mainRepository.upsert(
                            ConcertTicket(
                                type = EVENT,
                                payload = Payload(
                                    name = name,
                                    date = date,
                                    description = description,
                                    place = place,
                                    price = price.toInt(),
                                    quantity = quantity.toInt() - discountQuantity.toInt(),
                                    photo = TICKET_IMAGE,
                                    id = 99
                                )
                            )
                        )
                    )
                }
            } else {
                _eventFlow.emit(
                    mainRepository.upsert(
                        ConcertTicket(
                            type = EVENT,
                            payload = Payload(
                                name = name,
                                date = date,
                                description = description,
                                place = place,
                                price = price.toInt(),
                                quantity = quantity.toInt(),
                                photo = TICKET_IMAGE,
                                id = 99
                            )
                        )
                    )
                )
            }
        }
    }

}