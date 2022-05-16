package com.example.concerttickets.modules.concert_tickets.components.admin

import android.app.AlertDialog
import android.graphics.Typeface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.concerttickets.R
import com.example.concerttickets.databinding.FragmentAdminBinding
import com.example.concerttickets.modules.concert_tickets.components.admin.discount.DiscountFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdminFragment : Fragment() {

    private lateinit var binding: FragmentAdminBinding
    private val viewModel by viewModels<AdminViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //initViewPager
        initViewPager()

        setOnClickDiscount()

        //Init FAB onClick
        setAddOnClick()

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.admin_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.refresh_database -> {
                AlertDialog.Builder(binding.root.context)
                    .setMessage("Are you sure you want to reset Database?")
                    .setPositiveButton(getString(R.string.yes)) { _, _ ->
                        viewModel.resetDatabase()
                        Toast.makeText(binding.root.context, "Database Refreshed", Toast.LENGTH_SHORT)
                            .show()
                    }
                    .setNegativeButton(getString(R.string.cancel), null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setAddOnClick() {
        binding.adminAddFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_adminFragment_to_createFragment)
        }
    }

    private fun initViewPager() {
        //Disable scrolling
        binding.adminViewpager.isUserInputEnabled = false
        val pagerAdapter = AdminAdapter(requireActivity())
        binding.adminViewpager.adapter = pagerAdapter
    }

    private fun setOnClickDiscount() {
        val typeface = ResourcesCompat.getFont(binding.root.context, R.font.montserrat_regular)
        binding.adminNonDiscount.setOnClickListener {
            binding.adminViewpager.currentItem = 0
            binding.adminNonDiscount.setTypeface(typeface, Typeface.BOLD)
            binding.adminDiscount.setTypeface(typeface, Typeface.NORMAL)
        }
        binding.adminDiscount.setOnClickListener {
            binding.adminViewpager.currentItem = 1
            binding.adminNonDiscount.setTypeface(typeface, Typeface.NORMAL)
            binding.adminDiscount.setTypeface(typeface, Typeface.BOLD)
        }
    }

    class AdminAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount(): Int = 2
        override fun createFragment(position: Int): Fragment {
            return if (position == 0) DiscountFragment.newInstance(false)
            else DiscountFragment.newInstance(true)

        }
    }
}