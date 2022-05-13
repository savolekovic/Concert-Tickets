package com.example.concerttickets.modules.concert_tickets.components.create_edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.concerttickets.databinding.FragmentCreateEditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateEditFragment : Fragment() {

    private lateinit var binding: FragmentCreateEditBinding
    private val viewModel by viewModels<CreateEditViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as AppCompatActivity).supportActionBar?.title = "Create/Edit"

        binding = FragmentCreateEditBinding.inflate(inflater, container, false)

        return binding.root
    }

}