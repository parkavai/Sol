package com.example.soleklart.ui.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.soleklart.R
import com.example.soleklart.databinding.CalendarFragmentBinding
import com.example.soleklart.ui.SharedViewModel
import com.example.soleklart.ui.maps.MapsFragment
import com.example.soleklart.utils.getCalendarDaysFromToday
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment containing Calendar.
 * updates date in sharedViewModel on date selected
 */
class CalendarFragment : Fragment(R.layout.calendar_fragment) {
    private lateinit var binding: CalendarFragmentBinding
    private val viewModel: SharedViewModel by activityViewModels()

    /*
        OnViewCreated is the same as onCreate for Activities
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CalendarFragmentBinding.bind(view)
        setupCalendar()
    }

    /**
     * sets calendar range, and sets onclick listener for on date selected
     */
    private fun setupCalendar() {
        val c = Calendar.getInstance()
        binding.calendarView.minDate = c.timeInMillis
        val cw = getCalendarDaysFromToday(7)
        binding.calendarView.maxDate = cw.timeInMillis
        //Set On click
        setUpListener()
    }

    /**
     * sets onclick listener for on date selected
     * closes fragment and updates date in sharedViewModel
     */
    @SuppressLint("SimpleDateFormat")
    private fun setUpListener() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            // Update date in sharedViewModel
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date = formatter.format(calendar.time)
            viewModel.setDate(date)

            // Hide fragment
            val mapsFragment = parentFragment as MapsFragment
            mapsFragment.hideCalendar()
        }
    }
}