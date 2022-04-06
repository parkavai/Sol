package com.example.himmeltitting.utils
import com.example.himmeltitting.R

var mapId = 0

fun changeMapTheme(id: Int){
    mapId = id
}

fun getMapIdTheme(): Int{
    var theme = 0
    when(mapId){
        0 -> theme = R.raw.retro_style
        1 -> theme = R.raw.dark_style
        2 -> theme = R.raw.standard_style
    }
    return theme
}