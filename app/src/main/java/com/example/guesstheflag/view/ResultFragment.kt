package com.example.guesstheflag.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.guesstheflag.databinding.FragmentResultBinding


class ResultFragment : Fragment() {
    private lateinit var _binding: FragmentResultBinding
    private lateinit var navController: NavController
    private lateinit var args: ResultFragmentArgs

    private var score:Int = 0
    private lateinit var userName:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        //safeargs
        args = ResultFragmentArgs.fromBundle(requireArguments())
        score = args.resultScore
        userName = args.userName

        _binding.nameTv.text = "Well done $userName!"
        _binding.scoreTv.text = "You scored $score pts!"

        _binding.playAgainButton.setOnClickListener {
            val action = ResultFragmentDirections.actionResultFragmentToInfosFragment()
            navController.navigate(action)
        }


    }

}