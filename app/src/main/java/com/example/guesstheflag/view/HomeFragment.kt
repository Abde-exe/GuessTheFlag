package com.example.guesstheflag.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.guesstheflag.R
import com.example.guesstheflag.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var _binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.playButton.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_home2_to_infosFragment)
        }
    }

}