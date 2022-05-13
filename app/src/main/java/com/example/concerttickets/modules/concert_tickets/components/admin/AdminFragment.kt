package com.example.concerttickets.modules.concert_tickets.components.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.concerttickets.databinding.FragmentAdminBinding
import com.example.concerttickets.modules.concert_tickets.components.admin.discount.DiscountFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminFragment : Fragment() {

    private lateinit var binding: FragmentAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Disable scrolling
        binding.adminViewpager.isUserInputEnabled = false
        val pagerAdapter = AdminAdapter(requireActivity())
        binding.adminViewpager.adapter = pagerAdapter

        setOnClickDiscount()

    }

    private fun setOnClickDiscount() {
        binding.adminNonDiscount.setOnClickListener {
            binding.adminViewpager.currentItem = 0
        }
        binding.adminDiscount.setOnClickListener {
            binding.adminViewpager.currentItem = 1
        }
    }

    class AdminAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = 2
        override fun createFragment(position: Int): Fragment {
            return if (position == 0) DiscountFragment.newInstance(true)
            else DiscountFragment.newInstance(false)

        }
    }
}