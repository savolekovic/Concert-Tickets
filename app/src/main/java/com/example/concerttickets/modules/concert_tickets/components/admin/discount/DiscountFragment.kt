package com.example.concerttickets.modules.concert_tickets.components.admin.discount

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.concerttickets.R
import com.example.concerttickets.databinding.FragmentDiscountBinding
import com.example.concerttickets.modules.concert_tickets.adapters.UpcomingAdapter
import com.example.concerttickets.modules.concert_tickets.components.admin.AdminFragmentDirections
import com.example.concerttickets.utils.DISCOUNT
import com.example.concerttickets.utils.EVENT
import com.example.concerttickets.utils.IS_DISCOUNTED
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class DiscountFragment : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance(isDiscounted: Boolean) =
            DiscountFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_DISCOUNTED, isDiscounted)
                }
            }
    }

    private var isDiscounted: Boolean = false

    private lateinit var binding: FragmentDiscountBinding
    private val viewModel by viewModels<DiscountViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isDiscounted = requireArguments().getBoolean(IS_DISCOUNTED)
    }

    @Inject
    @Named("admin")
    lateinit var adapter: UpcomingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.adminDiscountTv.text = if (isDiscounted) getString(R.string.no_tickets_discount)
        else getString(R.string.no_tickets)

        //On ITEM click listener
        adapter.setOnItemClickListener {
            binding.root.findNavController()
                .navigate(AdminFragmentDirections.actionAdminFragmentToDetailsFragment(it.ticket_id!!))
        }
        //On DELETE click listener
        adapter.setOnDeleteClickListener {
            AlertDialog.Builder(binding.root.context)
                .setMessage("Delete this concert ticket?")
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.deleteConcertTicket(it)
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }
        //On EDIT click listener
        adapter.setOnEditClickListener {
            binding.root.findNavController().navigate(
                AdminFragmentDirections.actionAdminFragmentToCreateEditFragment(
                    ticketId = it.ticket_id!!
                )
            )
        }
        binding.adminRecycler.layoutManager = LinearLayoutManager(context)
        binding.adminRecycler.adapter = adapter

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.getDiscountedTickets(
            if (isDiscounted) DISCOUNT
            else EVENT
        ).observe(viewLifecycleOwner) {
            when (it.size) {
                0 -> binding.adminDiscountTv.visibility = View.VISIBLE
                else -> binding.adminDiscountTv.visibility = View.GONE
            }
            adapter.submitList(it, true)
        }
    }
}