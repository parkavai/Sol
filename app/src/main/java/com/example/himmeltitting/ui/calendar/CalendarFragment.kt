package com.example.himmeltitting.ui.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.himmeltitting.R
import com.example.himmeltitting.databinding.CalendarFragmentBinding
import com.example.himmeltitting.ui.SharedViewModel
import com.example.himmeltitting.ui.maps.MapsFragment
import java.text.SimpleDateFormat
import java.util.*

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

    private fun setupCalendar() {
        val c = getToday()
        binding.calendarView.minDate = c.timeInMillis
        val cw = getDateFromToday(7)
        binding.calendarView.maxDate = cw.timeInMillis
        //When u click on a date, do something
        setUpListener()
    }

    @SuppressLint("SimpleDateFormat")
    private fun setUpListener() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->

            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date = formatter.format(calendar.time)
            viewModel.setDate(date)
            Log.d("Date", date)
            val mapsFragment = parentFragment as MapsFragment
            mapsFragment.hideCalendar()
        }
    }

    //Helpmethod to find todays date in the right format
    private fun getToday(): Calendar {
        val calendar = Calendar.getInstance()

        val c = Calendar.getInstance()
        val dag = c.get(Calendar.DAY_OF_MONTH)
        val maaned = c.get(Calendar.MONTH)
        val aar = c.get(Calendar.YEAR)

        calendar.set(Calendar.MONTH, maaned)
        calendar.set(Calendar.DAY_OF_MONTH, dag)
        calendar.set(Calendar.YEAR, aar)

        return calendar
    }

    //Helpmethod to find a date X days from today
    private fun getDateFromToday(days: Int): Calendar {
        val calendar = Calendar.getInstance()

        val c = Calendar.getInstance()
        val dag = c.get(Calendar.DAY_OF_MONTH)
        val maaned = c.get(Calendar.MONTH)
        val aar = c.get(Calendar.YEAR)

        calendar.set(Calendar.MONTH, maaned)
        calendar.set(Calendar.DAY_OF_MONTH, dag + days)
        calendar.set(Calendar.YEAR, aar)

        return calendar
    }
}