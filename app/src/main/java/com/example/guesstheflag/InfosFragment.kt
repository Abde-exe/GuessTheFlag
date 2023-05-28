package com.example.guesstheflag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.guesstheflag.databinding.FragmentInfosBinding


class InfosFragment : Fragment() {
private lateinit var _binding: FragmentInfosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_infos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentInfosBinding.bind(view)

        _binding.startButton.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_infosFragment_to_gameFragment)
        }

    }

}