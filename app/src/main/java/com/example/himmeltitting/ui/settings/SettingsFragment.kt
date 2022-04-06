package com.example.himmeltitting.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.example.himmeltitting.databinding.FragmentSettingsBinding
import com.example.himmeltitting.utils.changeMapTheme

var idDarkMode = 0


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(inflater: LayoutInflater, container:  ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switchDarkMode(binding.switchDarkMode)
    }

}


/**
 * Function for setting darkmode or not depending on the state of the switch
 */
private fun switchDarkMode(switch: SwitchCompat){
    switch.setOnCheckedChangeListener { compoundButton, b ->
        checkSwitch(b)
    }
}

/**
 * Checks if the switch is "checked" or not
 */
private fun checkSwitch(isChecked: Boolean){
    if(isChecked){
        idDarkMode = 1
        changeMapTheme(idDarkMode)
    }
    else{
        idDarkMode = 0
        changeMapTheme(idDarkMode)
    }
}