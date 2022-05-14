package com.example.concerttickets.modules.concert_tickets.components.create_edit

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.concerttickets.databinding.FragmentCreateEditBinding
import com.example.concerttickets.modules.concert_tickets.models.ConcertTicket
import com.example.concerttickets.utils.Helper
import com.example.concerttickets.utils.IMAGE_URL
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class CreateEditFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentCreateEditBinding
    private val viewModel by viewModels<CreateEditViewModel>()

    private var ticketId = -1
    private var isEdit = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val args = arguments?.let { CreateEditFragmentArgs.fromBundle(it) }
        isEdit = args!!.isEdit
        if (isEdit)
            ticketId = args.ticketId
        (activity as AppCompatActivity).supportActionBar?.title =
            if (isEdit) "Edit" else "Create"

        binding = FragmentCreateEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getDetails(ticketId).observe(viewLifecycleOwner) {
            updateUI(it)
        }
    }

    private fun updateUI(it: ConcertTicket) {
        binding.apply {
            //Set image
            Glide.with(root.context)
                .load(IMAGE_URL + it.payload.photo)
                .into(createEditImg)
            //Name
            createEditName.setText(it.payload.name)
            //Date
            createEditDate.setText(it.payload.date)
            createEditDate.setOnClickListener {
                val dateSplit = createEditDate.text?.split(".")
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, dateSplit?.get(2)!!.toInt())
                calendar.set(Calendar.MONTH, dateSplit[1].toInt() - 1)
                calendar.set(Calendar.DAY_OF_MONTH, dateSplit[0].toInt())

                DatePickerDialog(
                    requireContext(),
                    this@CreateEditFragment,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            //Description
            if (it.payload.description != null)
                createEditDescription.setText(it.payload.description)
            //Location/place
            if (it.payload.place != null)
                createEditPlace.setText(it.payload.place)
            //Price
            createEditPrice.setText(it.payload.price.toString())
            //Quantity
            createEditQuantity.setText(it.payload.quantity.toString())
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        binding.createEditDate.setText(Helper.formatDate(year = year, month = month, day = day))
    }
}