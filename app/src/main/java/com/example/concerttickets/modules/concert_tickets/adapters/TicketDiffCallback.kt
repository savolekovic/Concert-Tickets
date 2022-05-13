package com.example.concerttickets.modules.concert_tickets.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.concerttickets.modules.concert_tickets.models.ConcertTicket

class TicketDiffCallback : DiffUtil.ItemCallback<ConcertTicket>() {
    override fun areItemsTheSame(oldItem: ConcertTicket, newItem: ConcertTicket): Boolean =
        oldItem.ticket_id == newItem.ticket_id

    override fun areContentsTheSame(oldItem: ConcertTicket, newItem: ConcertTicket): Boolean =
        oldItem == newItem
}