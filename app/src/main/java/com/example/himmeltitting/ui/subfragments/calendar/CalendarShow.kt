package com.example.himmeltitting.ui.subfragments.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.himmeltitting.R
import com.example.himmeltitting.databinding.CalendarBinding
import java.util.*

class CalendarShow : Fragment(R.layout.calendar) {
    private lateinit var binding: CalendarBinding

    /*
    OnViewCreated is the same as onCreate for Activities
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CalendarBinding.bind(view)

        setupCalendar()
    }

    private fun setupCalendar() {
        val c = getToday()
        binding.calendarView.setMinDate(c.timeInMillis)
        val cw = getDateFromToday(7)
        binding.calendarView.setMaxDate(cw.timeInMillis)
        //When u click on a date, do something
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

            // Note that months are indexed from 0. So, 0 means January, 1 means february, 2 means march etc.
            val dato = "Valgt dato er: " + dayOfMonth + "/" + (month + 1) + "/" + year

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