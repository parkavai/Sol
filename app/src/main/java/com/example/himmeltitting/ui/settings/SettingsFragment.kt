package com.example.himmeltitting.ui.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.example.himmeltitting.databinding.FragmentSettingsBinding
import com.example.himmeltitting.utils.changeMapTheme

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val arraySwitches = ArrayList<SwitchCompat>()
    private var idSwitchMode = 0

    override fun onCreateView(inflater: LayoutInflater, container:  ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textNight = binding.textNightTheme.text.toString()
        val switchNight = binding.switchNight

        val textStandard = binding.textStandardTheme.text.toString()
        val switchStandard = binding.switchStandard

        val textRetro = binding.textRetroTheme.text.toString()
        val switchRetro = binding.switchRetro

        arraySwitches.add(switchNight)
        arraySwitches.add(switchStandard)
        arraySwitches.add(switchRetro)

        switchMapTheme(switchNight, textNight)
        switchMapTheme(switchStandard, textStandard)
        switchMapTheme(switchRetro, textRetro)
    }

    /**
     * Function for setting darkmode or not depending on the state of the switch
     */
    private fun switchMapTheme(switch: SwitchCompat, textMode: String){
        switch.setOnCheckedChangeListener { compoundButton, b ->
            checkSwitch(textMode)
            changeAllSwitches(switch, arraySwitches, b)
            changeMapTheme(idSwitchMode)
        }
    }

    /**
     * Checks if the switch is "checked" or not
     */
    private fun checkSwitch(modeText: String){
        if(modeText == "Retro"){
            idSwitchMode = 0
        }
        else if(modeText == "Standard"){
            idSwitchMode = 1
        }
        else{
            idSwitchMode = 2
        }
    }

    private fun changeAllSwitches(clickedSwitch: SwitchCompat, arraySwitches: ArrayList<SwitchCompat>, isChecked: Boolean){
        for(switch in arraySwitches){
            if(switch != clickedSwitch && isChecked){
                switch.isEnabled = false
            }
            else if(switch != clickedSwitch && !isChecked){
                switch.isEnabled = true
            }
        }
    }

}


