package com.example.himmeltitting.ui.subfragments.bottomsheet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.himmeltitting.databinding.BottomSheetBinding
import com.example.himmeltitting.ui.SharedViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior


class BottomSheetFragment : Fragment() {
    private lateinit var binding: BottomSheetBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var bottomSheetViewModelFactory : BottomSheetViewModelFactory
    private lateinit var viewModel : BottomSheetViewModel
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
        bottomSheetViewModelFactory = BottomSheetViewModelFactory(sharedViewModel)
        viewModel = bottomSheetViewModelFactory.create()
        setBottomSheetVisibility(false)
        showData()
        lookForData()
    }

    private fun lookForData() {
        sharedViewModel.niluData.observe(viewLifecycleOwner){
            viewModel.loadDataOutput()
        }
    }
    private fun showData() {
        viewModel.outData.observe(viewLifecycleOwner){
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

