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
        val darkModeText = binding.textDarkMode.text.toString()
        val darkModeSwitch = binding.switchDarkMode
        val standardModeText = binding.textStandardMode.text.toString()
        val standardModeSwitch = binding.switchStandardMode
        arraySwitches.add(darkModeSwitch)
        arraySwitches.add(standardModeSwitch)
        switchMapTheme(darkModeSwitch, darkModeText)
        switchMapTheme(standardModeSwitch, standardModeText)
    }

    /**
     * Function for setting darkmode or not depending on the state of the switch
     */
    private fun switchMapTheme(switch: SwitchCompat, textMode: String){
        switch.setOnCheckedChangeListener { compoundButton, b ->
            checkSwitch(b, switch, textMode)
            changeAllSwitches(switch, arraySwitches, b)
            changeMapTheme(idSwitchMode)
        }
    }

    /**
     * Checks if the switch is "checked" or not
     */

    // Feilen er at du ikke sender TextViewet til hverken darkmode eller standardmode, du bruker switchen sitt textview som er feil
    private fun checkSwitch(isChecked: Boolean, switch: SwitchCompat, modeText: String){
        var id = 0
        if(modeText == "Dark"){
            id = 1
        }
        else if(modeText == "Standard"){
            id = 2
        }
        isSwitchEnabled(isChecked, id)
    }

    private fun isSwitchEnabled(isChecked:Boolean, id: Int){
        if(isChecked){
            idSwitchMode = id
        }
        else{
            idSwitchMode = 0
        }
    }

    private fun changeAllSwitches(clickedSwitch: SwitchCompat, arraySwitches: ArrayList<SwitchCompat>, isChecked: Boolean){
        for (switch in arraySwitches){
            if(isChecked){
                if(switch != clickedSwitch){
                    switch.isEnabled = false
                }
            }
            else{
                if(switch != clickedSwitch){
                    switch.isEnabled = true
                }
            }
        }
    }

}


