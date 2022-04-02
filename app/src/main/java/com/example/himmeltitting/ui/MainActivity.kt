package com.example.himmeltitting.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.himmeltitting.SharedViewModel
import com.example.himmeltitting.R
import com.example.himmeltitting.databinding.ActivityMainBinding
import com.example.himmeltitting.ui.favorites.FavoritesFragment
import com.example.himmeltitting.ui.info.InfoFragment
import com.example.himmeltitting.ui.maps.MapsFragment
import com.example.himmeltitting.ui.settings.SettingsFragment
import com.example.himmeltitting.ui.subfragments.calendar.CalendarShow


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapsFragment = MapsFragment()
        val favoritesFragment = FavoritesFragment()
        val settingsFragment = SettingsFragment()
        val infoFragment = InfoFragment()
        val calendar = CalendarShow()

        makeCurrentFragment(mapsFragment)

        //legge til egen metode
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.search -> {
                    makeCurrentFragment(mapsFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.fav -> {
                    makeCurrentFragment(favoritesFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.info -> {
                    makeCurrentFragment(infoFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.settings -> {
                    makeCurrentFragment(settingsFragment)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    // initializes fragments from navigation bar
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mapsFragment, fragment)
            commit()
        }


}