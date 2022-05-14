package com.example.concerttickets.modules.concert_tickets.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.concerttickets.databinding.RowDiscountBinding
import com.example.concerttickets.modules.concert_tickets.models.ConcertTicket
import com.example.concerttickets.utils.IMAGE_URL
import java.text.NumberFormat
import java.util.*

class DiscountAdapter :
    ListAdapter<ConcertTicket, DiscountAdapter.DiscountViewHolder>(TicketDiffCallback()) {

    class DiscountViewHolder(private val binding: RowDiscountBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(concertTicket: ConcertTicket, onItemCLickListener: ((ConcertTicket) -> Unit)?) {

            binding.apply {
                root.setOnClickListener {
                    onItemCLickListener?.let { it(concertTicket) }
                }
                Glide.with(root.context)
                    .load(IMAGE_URL + concertTicket.payload.photo)
                    .into(discountImage)

                val discountText = "-${concertTicket.payload.discount}%"
                discountPercent.text = discountText

                if (concertTicket.payload.place == null) {
                    discountLocationImg.visibility = View.GONE
                    discountLocation.visibility = View.GONE
                } else
                    discountLocation.text = concertTicket.payload.place
                discountDate.text = concertTicket.payload.date

                val discountedPrice =
                    concertTicket.payload.price * ((100 - concertTicket.payload.discount!!) / 100.0f)
                val ticketsDiscountedText =
                    "Only ${concertTicket.payload.quantity} tickets for ${NumberFormat.getCurrencyInstance(
                        Locale.GERMANY
                    ).format(discountedPrice)}"
                discountTicketsCountPrice.text = ticketsDiscountedText

                discountPerformer.text = concertTicket.payload.name
            }
        }

        companion object {
            fun from(parent: ViewGroup): DiscountViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemBinding = RowDiscountBinding.inflate(layoutInflater, parent, false)
                return DiscountViewHolder(itemBinding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountViewHolder {
        return DiscountViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: DiscountViewHolder, position: Int) {
        holder.bind(currentList[position], onItemCLickListener)
    }

    override fun getItemCount(): Int = currentList.size

    private var onItemCLickListener: ((ConcertTicket) -> Unit)? = null
    fun setOnItemClickListener(listener: (ConcertTicket) -> Unit) {
        onItemCLickListener = listener
    }

}