package com.example.soleklart.ui.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.soleklart.databinding.FragmentInfoBinding

/**
 * A Fragment which is responsible for displaying "Sunfacts"-information.
 */
class InfoFragment : Fragment() {
    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

}