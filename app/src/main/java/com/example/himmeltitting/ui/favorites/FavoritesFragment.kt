package com.example.himmeltitting.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.himmeltitting.R
import com.example.himmeltitting.databinding.FragmentFavoritesBinding

class FavoritesFragment : Fragment() {

    private lateinit var binding: FragmentFavoritesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gaardboImage = binding.gaardboImage
        val toensbergImage = binding.toensbergImage
        val skienImage = binding.skienImage

        if (resources.getString(R.string.mode) == "Night") {
            changePictures(gaardboImage, toensbergImage, skienImage)
        }
    }

    /**
     * Change pictures when darkmode is changed
     */
    private fun changePictures(gaardboView: ImageView, toensbergView: ImageView, skienView: ImageView){
        gaardboView.setImageResource(R.drawable.gaardbo_dark)
        toensbergView.setImageResource(R.drawable.toensberg_dark)
        skienView.setImageResource(R.drawable.skien_dark)
    }
}