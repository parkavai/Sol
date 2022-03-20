package com.example.himmeltitting.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.himmeltitting.MapsActivityViewModel
import com.example.himmeltitting.databinding.BottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior


class BottomSheet : Fragment() {
    private lateinit var binding: BottomSheetBinding
    private val viewModel: MapsActivityViewModel by activityViewModels()
    private val bottomSheetView by lazy { binding.bottomSheet }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container:  ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetBinding.inflate(inflater, container, false)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setBottomSheetVisibility(false)
        showData()

    }

    private fun showData() {
        viewModel.getDataOutput().observe(viewLifecycleOwner){
            setBottomSheetVisibility(true)
            binding.dataTextView.text = it
        }
    }

    fun setBottomSheetVisibility(isVisible: Boolean) {
        Log.d("Bottom sheet visibility", true.toString())
        val updatedState = if (isVisible) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.state = updatedState
    }
}

