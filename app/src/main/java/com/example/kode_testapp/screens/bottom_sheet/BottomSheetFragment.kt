package com.example.kode_testapp.screens.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.kode_testapp.R
import com.example.kode_testapp.databinding.BottomSheetFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment: BottomSheetDialogFragment() {

    var binding: BottomSheetFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {binding = BottomSheetFragmentBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.sorting_by_birthday -> Toast.makeText(requireContext(), "sorting_by_birthday", Toast.LENGTH_SHORT).show()
                R.id.sort_alphabetically -> Toast.makeText(requireContext(), "sort_alphabetically", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}