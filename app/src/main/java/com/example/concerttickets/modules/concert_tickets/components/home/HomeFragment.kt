package com.example.concerttickets.modules.concert_tickets.components.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.concerttickets.R
import com.example.concerttickets.databinding.FragmentHomeBinding
import com.example.concerttickets.modules.concert_tickets.adapters.DiscountAdapter
import com.example.concerttickets.modules.concert_tickets.adapters.ExpiredAdapter
import com.example.concerttickets.modules.concert_tickets.adapters.UpcomingAdapter
import com.example.concerttickets.modules.concert_tickets.models.ConcertTicket
import com.example.concerttickets.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel>()

    @Inject
    lateinit var discountAdapter: DiscountAdapter

    @Inject
    @Named("upcoming")
    lateinit var upcomingAdapter: UpcomingAdapter

    @Inject
    lateinit var expiredAdapter: ExpiredAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerViews()

        //Get data
        subscribeObserver()

        binding.homeAdminFab.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_adminFragment)
        )

    }

    private fun initRecyclerViews() {
        //Discount
        discountAdapter.setOnItemClickListener {
            //Add Safe args here
            binding.root.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it.ticket_id!!))
        }
        binding.discountRecycler.adapter = discountAdapter

        //Upcoming
        upcomingAdapter.setOnItemClickListener {
            //Add Safe args here
            binding.root.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it.ticket_id!!))
        }
        binding.upcomingRecycler.layoutManager =
            GridLayoutManager(binding.root.context, 2, GridLayoutManager.HORIZONTAL, false)
        binding.upcomingRecycler.addItemDecoration(SimpleItemDecorator(16, true))
        binding.upcomingRecycler.adapter = upcomingAdapter

        //Upcoming
        expiredAdapter.setOnItemClickListener {
            //Add Safe args here
            binding.root.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(it.ticket_id!!))
        }
        binding.expiredRecycler.adapter = expiredAdapter
        binding.homeAdminFab.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_homeFragment_to_adminFragment)
        )
    }

    private fun subscribeObserver() {
        viewModel.tickets.observe(viewLifecycleOwner) {
            //We got all tickets from the API
            //Now we have to extract 3 separate lists (Discounts, Events and Expired)
            if (it is Resource.Success && !it.data.isNullOrEmpty()) {
                extractLists(it.data)
                binding.homeContainer.isVisible = true
            }
            binding.homeLoading.isVisible = it is Resource.Loading && it.data.isNullOrEmpty()
            binding.homeError.isVisible = it is Resource.Error && it.data.isNullOrEmpty()
            binding.homeError.text = it.error?.localizedMessage
        }
    }

    private fun extractLists(tickets: List<ConcertTicket>) {
        val discountList = ArrayList<ConcertTicket>()
        val upcomingList = ArrayList<ConcertTicket>()
        val expiredList = ArrayList<ConcertTicket>()

        tickets.forEach {
            if (it.type == DISCOUNT) {
                if (it.payload.date.isValidDate() == 1 || it.payload.date.isValidDate() == 0)
                    discountList.add(it)
                else
                    expiredList.add(it)
            } else if (it.type == EVENT && it.payload.date.isValidDate() == 1 || it.payload.date.isValidDate() == 0)
                upcomingList.add(it)
        }
        if (discountList.isEmpty()) {
            binding.discountTv.visibility = View.GONE
            binding.discountRecycler.visibility = View.GONE
        } else discountAdapter.submitList(discountList)

        if (upcomingList.isEmpty()) {
            binding.upcomingTv.visibility = View.GONE
            binding.upcomingRecycler.visibility = View.GONE
        } else upcomingAdapter.submitList(upcomingList, false)

        if (expiredList.isEmpty()) {
            binding.expiredTv.visibility = View.GONE
            binding.expiredRecycler.visibility = View.GONE
        } else expiredAdapter.submitList(expiredList)

        Log.d("test123", "discountList $discountList")
        Log.d("test123", "upcomingList $upcomingList")
        Log.d("test123", "expiredList $expiredList")
    }
}