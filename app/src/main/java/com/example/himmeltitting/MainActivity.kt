package com.example.himmeltitting

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.himmeltitting.databinding.ActivityMainBinding
import com.example.himmeltitting.fragments.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MapsActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapsFragment = Maps()
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