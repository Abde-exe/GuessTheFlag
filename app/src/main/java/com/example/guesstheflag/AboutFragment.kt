package com.example.guesstheflag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.room.Room
import com.example.guesstheflag.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private lateinit var _binding: FragmentAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentAboutBinding.inflate(layoutInflater)

        return inflater.inflate(R.layout.fragment_about, container, false)

    }


}