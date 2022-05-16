package com.example.concerttickets.modules.concert_tickets.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.concerttickets.databinding.RowExpiredBinding
import com.example.concerttickets.modules.concert_tickets.models.ConcertTicket
import com.example.concerttickets.utils.IMAGE_URL

class ExpiredAdapter :
    ListAdapter<ConcertTicket, ExpiredAdapter.ExpiredViewHolder>(TicketDiffCallback()) {

    class ExpiredViewHolder(private val binding: RowExpiredBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(concertTicket: ConcertTicket, onItemCLickListener: ((ConcertTicket) -> Unit)?) {
            binding.apply {

                root.setOnClickListener {
                    onItemCLickListener?.let { it(concertTicket) }
                }

                Glide.with(root.context)
                    .load(IMAGE_URL + concertTicket.payload.photo)
                    .into(expiredImage)

                val discountText = "-${concertTicket.payload.discount}%"
                expiredDiscount.text = discountText

                expiredPerformer.text = concertTicket.payload.name

                if (concertTicket.payload.place == null) {
                    expiredLocation.visibility = View.GONE
                } else {
                    expiredLocation.visibility = View.VISIBLE
                    expiredLocation.text = concertTicket.payload.place
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ExpiredViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemBinding = RowExpiredBinding.inflate(layoutInflater, parent, false)
                return ExpiredViewHolder(itemBinding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpiredViewHolder {
        return ExpiredViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ExpiredViewHolder, position: Int) {
        holder.bind(currentList[position], onItemCLickListener)
    }

    override fun getItemCount(): Int = currentList.size

    private var onItemCLickListener: ((ConcertTicket) -> Unit)? = null
    fun setOnItemClickListener(listener: (ConcertTicket) -> Unit) {
        onItemCLickListener = listener
    }

}