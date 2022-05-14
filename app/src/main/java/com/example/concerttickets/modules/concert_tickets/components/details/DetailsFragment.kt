package com.example.concerttickets.modules.concert_tickets.components.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.concerttickets.databinding.FragmentDetailsBinding
import com.example.concerttickets.utils.IMAGE_URL
import dagger.hilt.android.AndroidEntryPoint

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
            binding.apply {
                Glide.with(root.context)
                    .load(IMAGE_URL + it.payload.photo)
                    .into(detailsImg)
                detailsName.text = it.payload.name
                detailsDescription.text = it.payload.description
                detailsQuantity.text = it.payload.quantity.toString()

                //No time on the API so we set it to 11:30
                detailsTime.text = "11:30"
                if (it.payload.place != null)
                    detailsPlace.text = it.payload.place
                else {
                    detailsPlace.visibility = View.GONE
                    detailsPlaceHeader.visibility = View.GONE
                }
                var finalPrice: String = it.payload.price.toString()

                if (it.payload.discount != null) {
                    val discount = "${it.payload.discount}%"
                    detailsDiscount.text = discount

                    val discountedPrice =
                        it.payload.price * ((100 - it.payload.discount) / 100.0f)


                    finalPrice = "${String.format("%.1f", discountedPrice)} €"
                } else {
                    detailsDiscount.visibility = View.GONE
                    detailsDiscountHeader.visibility = View.GONE
                }

                detailsPrice.text = finalPrice
            }
        }
    }
}