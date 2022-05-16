package com.example.concerttickets.modules.concert_tickets.components.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.concerttickets.databinding.FragmentDetailsBinding
import com.example.concerttickets.modules.concert_tickets.models.ConcertTicket
import com.example.concerttickets.utils.IMAGE_URL
import com.example.concerttickets.utils.TIME_VALUE
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel by viewModels<DetailsViewModel>()

    var ticketId = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)

        val args = arguments?.let { DetailsFragmentArgs.fromBundle(it) }
        ticketId = args!!.ticketId

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDetails(ticketId).observe(viewLifecycleOwner) {
            updateUI(it)
        }
    }

    private fun updateUI(ticket: ConcertTicket) {
        binding.apply {
            Glide.with(root.context)
                .load(IMAGE_URL + ticket.payload.photo)
                .into(detailsImg)
            detailsName.text = ticket.payload.name

            //Description is optional(nullable)
            if (ticket.payload.description != null)
                detailsDescription.text = ticket.payload.description
            else
                detailsDescription.visibility = View.GONE

            detailsQuantity.text = ticket.payload.quantity.toString()

            detailsDate.text = ticket.payload.date

            //Location/place is nullable in the API
            if (ticket.payload.place != null)
                detailsPlace.text = ticket.payload.place
            else {
                detailsPlace.visibility = View.GONE
                detailsPlaceHeader.visibility = View.GONE
            }
            var finalPrice: Float = ticket.payload.price.toFloat()

            //Discount is nullable in the API
            if (ticket.payload.discount != null) {
                val discount = "${ticket.payload.discount}%"
                detailsDiscount.text = discount

                //Change final price if there is a discount
                finalPrice = ticket.payload.price * ((100 - ticket.payload.discount!!) / 100.0f)
            } else {
                detailsDiscount.visibility = View.GONE
                detailsDiscountHeader.visibility = View.GONE
            }

            detailsPrice.text = NumberFormat.getCurrencyInstance(Locale.GERMANY).format(finalPrice)
        }

    }
}