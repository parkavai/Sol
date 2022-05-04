package com.example.soleklart.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.soleklart.R
import com.example.soleklart.databinding.FragmentSettingsBinding
import com.example.soleklart.utils.changeMapTheme

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val arraySwitches = ArrayList<SwitchCompat>()
    private var idSwitchMode = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textStandard = binding.textStandardTheme.text.toString()
        val switchStandard = binding.switchStandard

        val textAubergine = binding.textAubergineTheme.text.toString()
        val switchAubergine = binding.switchAubergine

        val textNight = binding.textNightTheme.text.toString()
        val switchNight = binding.switchNight

        arraySwitches.add(switchAubergine)
        arraySwitches.add(switchStandard)
        arraySwitches.add(switchNight)

        switchMapTheme(switchAubergine, textAubergine)
        switchMapTheme(switchStandard, textStandard)
        switchMapTheme(switchNight, textNight)
        if (resources.getString(R.string.mode) == "Night") {
            disableSwitches(arraySwitches)
        }
    }

    /**
     * Function for changing the theme of the map depending on
     * switch which was clicked.
     */
    private fun switchMapTheme(switch: SwitchCompat, textMode: String) {
        switch.setOnCheckedChangeListener { _, b ->
            checkSwitch(textMode)
            changeAllSwitches(switch, arraySwitches, b)
            changeMapTheme(idSwitchMode)
        }
    }

    /**
     * Checks which switch was clicked in order to adjust the map theme
     */
    private fun checkSwitch(modeText: String) {
        idSwitchMode = when (modeText) {
            "Aubergine" -> {
                0
            }
            "Standard" -> {
                1
            }
            else -> {
                2
            }
        }
    }

    /**
     * Help method that disables switches if dark mode is enabled
     */
    private fun disableSwitches(arraySwitches: ArrayList<SwitchCompat>) {
        for (switch in arraySwitches) {
            switch.isEnabled = false
        }
    }

    /**
     * If a switch is "checked" then all the other switches except the one which was chosen,
     * are disabled. Otherwise, every switch are enabled and retro-style is the current map theme.
     */
    private fun changeAllSwitches(
        clickedSwitch: SwitchCompat,
        arraySwitches: ArrayList<SwitchCompat>,
        isChecked: Boolean
    ) {
        for (switch in arraySwitches) {
            if (switch != clickedSwitch && isChecked) {
                switch.isEnabled = false
            } else if (switch != clickedSwitch && !isChecked) {
                idSwitchMode = 3
                switch.isEnabled = true
            }
        }
    }
}


