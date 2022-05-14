package com.example.concerttickets.modules.concert_tickets.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.concerttickets.R
import com.example.concerttickets.databinding.RowUpcomingBinding
import com.example.concerttickets.modules.concert_tickets.models.ConcertTicket
import com.example.concerttickets.utils.IMAGE_URL
import com.example.concerttickets.utils.TIME_VALUE
import com.example.concerttickets.utils.toMonth
import java.text.NumberFormat
import java.util.*

class UpcomingAdapter :
    ListAdapter<ConcertTicket, UpcomingAdapter.UpcomingViewHolder>(TicketDiffCallback()) {

    private var isAdmin = false

    fun submitList(list: List<ConcertTicket>, isAdmin: Boolean) {
        this.isAdmin = isAdmin
        super.submitList(list)
    }

    class UpcomingViewHolder(private val binding: RowUpcomingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            concertTicket: ConcertTicket,
            isAdmin: Boolean,
            onItemClickListener: ((ConcertTicket) -> Unit)?,
            onDeleteClickListener: ((ConcertTicket) -> Unit)?,
            onEditClickListener: ((ConcertTicket) -> Unit)?
        ) {
            binding.apply {
                if (isAdmin) {
                    adminCard.visibility = View.VISIBLE
                    upcomingContainer.setBackgroundColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.app_color
                        )
                    )
                    if (concertTicket.payload.discount != null) {
                        upcomingDiscount.visibility = View.VISIBLE
                        val discountText = "-${concertTicket.payload.discount}%"
                        upcomingDiscount.text = discountText

                        val discountedPrice =
                            concertTicket.payload.price * ((100 - concertTicket.payload.discount) / 100.0f)
                        val ticketsDiscountedText = "${String.format("%.1f", discountedPrice)}€"
                        upcomingPrice.text = ticketsDiscountedText
                    } else {
                        val priceText = "${concertTicket.payload.price} €"
                        upcomingPrice.text = priceText
                    }

                    adminDelete.setOnClickListener {
                        onDeleteClickListener?.let { it(concertTicket) }
                    }
                    adminEdit.setOnClickListener {
                        onEditClickListener?.let { it(concertTicket) }
                    }
                } else {
                    upcomingPrice.text = NumberFormat.getCurrencyInstance(Locale.GERMANY)
                        .format(concertTicket.payload.price)
                }

                root.setOnClickListener {
                    onItemClickListener?.let { it(concertTicket) }
                }

                val dateSplit = concertTicket.payload.date.split(".")
                //We parse it to remove 0, for example if it returns 04, we just need to show 4
                upcomingDay.text = dateSplit[0].toInt().toString()
                upcomingMonth.text = dateSplit[1].toMonth()
                upcomingYear.text = dateSplit[2]

                Glide.with(root.context)
                    .load(IMAGE_URL + concertTicket.payload.photo)
                    .into(upcomingImg)

                upcomingPerformer.text = concertTicket.payload.name

                //There is no time in APi, so I have put 11:30AM for every item
                upcomingTime.text = TIME_VALUE

                if (concertTicket.payload.place == null) {
                    upcomingLocation.visibility = View.GONE
                } else
                    upcomingLocation.text = concertTicket.payload.place

                val quantityText = "${concertTicket.payload.quantity} tickets more"
                upcomingTickets.text = quantityText
            }
        }

        companion object {
            fun from(parent: ViewGroup): UpcomingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemBinding = RowUpcomingBinding.inflate(layoutInflater, parent, false)
                return UpcomingViewHolder(itemBinding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpcomingViewHolder {
        return UpcomingViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: UpcomingViewHolder, position: Int) {
        holder.bind(
            currentList[position],
            isAdmin,
            onItemClickListener,
            onDeleteClickListener,
            onEditClickListener
        )
    }

    override fun getItemCount(): Int = currentList.size

    private var onItemClickListener: ((ConcertTicket) -> Unit)? = null
    fun setOnItemClickListener(listener: (ConcertTicket) -> Unit) {
        onItemClickListener = listener
    }

    private var onDeleteClickListener: ((ConcertTicket) -> Unit)? = null
    fun setOnDeleteClickListener(listener: (ConcertTicket) -> Unit) {
        onDeleteClickListener = listener
    }

    private var onEditClickListener: ((ConcertTicket) -> Unit)? = null
    fun setOnEditClickListener(listener: (ConcertTicket) -> Unit) {
        onEditClickListener = listener
    }

}