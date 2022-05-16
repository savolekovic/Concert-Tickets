package com.example.concerttickets.modules.concert_tickets.components.create_edit

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.concerttickets.R
import com.example.concerttickets.databinding.FragmentCreateEditBinding
import com.example.concerttickets.utils.Helper
import com.example.concerttickets.utils.IMAGE_URL
import com.example.concerttickets.utils.TICKET_IMAGE
import com.example.concerttickets.utils.getStringOrNull
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*


@AndroidEntryPoint
class CreateFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentCreateEditBinding
    private val viewModel by viewModels<CreateEditViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title = "Create"

        binding = FragmentCreateEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDiscountToggle()
        observeViewModel()
        initDatePicker()

        Glide.with(binding.root.context)
            .load(IMAGE_URL + TICKET_IMAGE)
            .into(binding.createEditImg)
        binding.createEditSubmit.text = getString(R.string.create)
        binding.createEditSubmit.setOnClickListener {
            viewModel.create(
                binding.createEditName.text.toString().trim(),
                binding.createEditDescription.text.toString().trim(),
                binding.createEditPlace.text.toString().trim(),
                binding.createEditDate.text.toString().trim(),
                binding.createEditPrice.text.toString().trim(),
                binding.createEditQuantity.text.toString().trim(),
                binding.createEditDiscountValue.text.toString().trim(),
                binding.createEditDiscountQuantity.text.toString().trim(),
                binding.createEditDiscountToggle.isChecked
            )
        }
    }

    private fun initDatePicker() {
        binding.createEditDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenCreated {
            viewModel.eventFlow.collectLatest {
                if (it > 0) {
                    binding.root.findNavController().navigateUp()
                    Toast.makeText(
                        binding.root.context,
                        "New Concert tickets created.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        //Name
        lifecycleScope.launchWhenCreated {
            viewModel.nameErrorFlow.collectLatest {
                binding.createEditName.error = getStringOrNull(it)
            }
        }
        //Place
        lifecycleScope.launchWhenCreated {
            viewModel.placeErrorFlow.collectLatest {
                binding.createEditPlace.error = getStringOrNull(it)
            }
        }
        //Date
        lifecycleScope.launchWhenCreated {
            viewModel.dateErrorFlow.collectLatest {
                binding.createEditDate.error = getStringOrNull(it)
            }
        }
        //Price
        lifecycleScope.launchWhenCreated {
            viewModel.priceErrorFlow.collectLatest {
                binding.createEditPrice.error = getStringOrNull(it)
            }
        }
        //Quantity
        lifecycleScope.launchWhenCreated {
            viewModel.quantityErrorFlow.collectLatest {
                binding.createEditQuantity.error = getStringOrNull(it)
            }
        }
        //DiscountValue
        lifecycleScope.launchWhenCreated {
            viewModel.discountValueErrorFlow.collectLatest {
                binding.createEditDiscountValue.error = getStringOrNull(it)
            }
        }
//        //DiscountQuantity
//        lifecycleScope.launchWhenCreated {
//            viewModel.discountQuantityErrorFlow.collectLatest {
//                binding.createEditDiscountQuantity.error = getStringOrNull(it)
//            }
//        }
    }

    private fun initDiscountToggle() {
        binding.createEditDiscountToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.createEditDiscountCard.visibility = View.VISIBLE
            } else {
                binding.createEditDiscountCard.visibility = View.GONE
            }
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        binding.createEditDate.setText(Helper.formatDate(year = year, month = month, day = day))
    }
}