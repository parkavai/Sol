package com.example.himmeltitting

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.himmeltitting.databinding.ActivityMainBinding
import com.example.himmeltitting.fragments.FavoritesFragment
import com.example.himmeltitting.fragments.InfoFragment
import com.example.himmeltitting.fragments.Maps
import com.example.himmeltitting.fragments.SettingsFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MapsActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapsFragment = Maps()
        val favoritesFrafment = FavoritesFragment()
        val settingsFragment = SettingsFragment()
        val infoFragment = InfoFragment()

        makeCuurentFragment(mapsFragment)

        //legge til egen metode
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.search -> {
                    makeCuurentFragment(mapsFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.fav -> {
                    makeCuurentFragment(favoritesFrafment)
                    return@setOnItemSelectedListener true
                }
                R.id.info -> {
                    makeCuurentFragment(infoFragment)
                    return@setOnItemSelectedListener true
                }
                R.id.settings -> {
                    makeCuurentFragment(settingsFragment)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    // initializes fragments from navigation bar
    private fun makeCuurentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mapsFragment, fragment)
            commit()
        }


}