package com.example.himmeltitting.ui.bottomsheet

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
import com.example.himmeltitting.utils.prettyTimeString
import com.google.android.material.bottomsheet.BottomSheetBehavior


class BottomSheetFragment : Fragment() {
    private lateinit var binding: BottomSheetBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
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
        viewModel = BottomSheetViewModel(sharedViewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setBottomSheetVisibility(false)
        observeState()
        showData()
        showDataUnSynced()
    }

    /**
     * Observes shared viewmodel data fetching state,
     * sets intermediate loading bar visibility according to state
     * and makes bottom sheet viewmodel update strings with new data
     */
    private fun observeState() {
        sharedViewModel.state.observe(viewLifecycleOwner){
            if(it == "loading"){
                binding.indeterminateBar.visibility = View.VISIBLE
            }
            else if (it == "finished"){
                viewModel.loadDataOutput()
                binding.indeterminateBar.visibility = View.INVISIBLE
            }
        }
    }

    /**
     * Observes string with data from bottom sheet viewmodel
     */
    private fun showData() {
        viewModel.outData.observe(viewLifecycleOwner){
            setBottomSheetVisibility(true)
            //binding.dataTextView.text = it
        }
    }

    /**
     * Makes bottom sheet visible/popup or invisible/popdown
     * with values true of false
     */
    private fun setBottomSheetVisibility(isVisible: Boolean) {
        Log.d("Bottom sheet visibility", true.toString())
        val updatedState = if (isVisible) BottomSheetBehavior.STATE_EXPANDED else BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.state = updatedState
    }

    private fun showDataUnSynced() {
        sharedViewModel.niluData.observe(viewLifecycleOwner) {
            binding.airTextUp.text = it?.airQualitySunrise.toString()
            binding.airTextDown.text = it?.airQualitySunset.toString()
        }
        sharedViewModel.sunriseData.observe(viewLifecycleOwner) {
            binding.topHeader.text = "Soloppgang ${prettyTimeString(it.sunriseTime!!)}"
            binding.botHeader.text = "Solnedgang ${prettyTimeString(it.sunsetTime!!)}"
        }
        sharedViewModel.sunriseForecast.observe(viewLifecycleOwner) {
            binding.temperatureTextUp.text = it?.temperature ?: "None"
            binding.windTextUp.text = it?.wind_speed ?: "None"
            binding.rainTextUp.text = it?.precipitation6Hours ?: "None"
            binding.cloudTextUp.text = it?.cloudCover ?: "None"
        }
        sharedViewModel.sunsetForecast.observe(viewLifecycleOwner) {
            binding.temperatureTextDown.text = it?.temperature ?: "None"
            binding.windTextDown.text = it?.wind_speed ?: "None"
            binding.rainTextDown.text = it?.precipitation6Hours ?: "None"
            binding.cloudTextDown.text = it?.cloudCover ?: "None"
        }

    }
}

