package com.example.guesstheflag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.guesstheflag.databinding.FragmentInfosBinding


class InfosFragment : Fragment() {
private lateinit var _binding: FragmentInfosBinding
private lateinit var nameET : EditText
private lateinit var startBtn : Button
private lateinit var radioGroup:RadioGroup
private var region : String = ""


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
        val navController = Navigation.findNavController(view)
        _binding = FragmentInfosBinding.bind(view)

        startBtn = _binding.startButton
        radioGroup = _binding.radioGroup
        nameET = _binding.editTextName




        //radiogroup
        radioGroup.apply {
            setOnCheckedChangeListener{_, checkedId ->
                startBtn.isEnabled = checkedId != -1
            }
        }


        //start button
        startBtn.setOnClickListener {
            val checkedId = radioGroup.checkedRadioButtonId

            if (checkedId != -1) {
                val checkedRadioButton = radioGroup.findViewById<View>(checkedId)
                val checkedIndex = radioGroup.indexOfChild(checkedRadioButton)
                region = when (checkedIndex) {
                    0 -> "Europe"
                    1 -> "Africa"
                    2 -> "America"
                    3 -> "Asia"
                    else -> "Worldwide"
                }

            }
            else{
                Toast.makeText(context, "Please select a region", Toast.LENGTH_SHORT).show()
            }


            if(nameET.text.isNotEmpty()){
                val name = nameET.text.toString()
                val action = InfosFragmentDirections.actionInfosFragmentToGameFragment(name, region)
                navController.navigate(action)

            }
            else{
                Toast.makeText(context, "Please enter your name", Toast.LENGTH_SHORT).show()
            }

        }

    }

}