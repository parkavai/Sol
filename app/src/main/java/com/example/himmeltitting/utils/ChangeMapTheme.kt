package com.example.himmeltitting.utils
import com.example.himmeltitting.R

private var theme = R.raw.retro_style

/**
 * Based on which theme was chosen, an id will be given which
 * represents the chosen theme.
 */
fun changeMapTheme(id: Int){
    when(id){
        0 -> theme = R.raw.aubergine_style
        1 -> theme = R.raw.standard_style
        2 -> theme = R.raw.night_style
        3 -> theme = R.raw.retro_style
    }
}

/**
 * Returns the chosen theme
 */
fun getChosenTheme(): Int{
    return theme
}