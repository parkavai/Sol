package com.example.himmeltitting.utils
import com.example.himmeltitting.R

private var themeId = 0

/**
 * Based on which theme was chosen, an id will be given which
 * represents the chosen theme.
 */
fun changeMapTheme(id: Int){
    themeId = id
}

/**
 * Returns the chosen theme based on the value of themeId.
 */
fun getChosenTheme(): Int{
    var theme = 0
    when(themeId){
        0 -> theme = R.raw.retro_style
        1 -> theme = R.raw.standard_style
        2 -> theme = R.raw.night_style
    }
    return theme
}