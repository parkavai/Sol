package com.example.soleklart.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.soleklart.R
import com.example.soleklart.databinding.ActivityMainBinding
import com.example.soleklart.ui.favorites.FavoritesFragment
import com.example.soleklart.ui.info.InfoFragment
import com.example.soleklart.ui.maps.MapsFragment
import com.example.soleklart.ui.settings.SettingsFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapsFragment = MapsFragment()
        val favoritesFragment = FavoritesFragment()
        val settingsFragment = SettingsFragment()
        val infoFragment = InfoFragment()

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