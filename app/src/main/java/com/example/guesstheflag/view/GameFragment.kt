package com.example.guesstheflag.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.room.Room
import com.example.guesstheflag.*
import com.example.guesstheflag.database.AppDatabase
import com.example.guesstheflag.databinding.FragmentGameBinding
import com.example.guesstheflag.model.Question
import com.example.guesstheflag.network.RetrofitInstance
import com.example.guesstheflag.viewmodel.GameViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import retrofit2.Retrofit
import java.util.*
import kotlin.collections.ArrayList


class GameFragment : Fragment() {
    private lateinit var _binding:FragmentGameBinding
    private lateinit var viewModel: GameViewModel
    private lateinit var navController:NavController
    private lateinit var args: GameFragmentArgs

    private lateinit var region:String
    private lateinit var userName:String
    private  var questions:ArrayList<Question> = ArrayList()
    private  var countries:ArrayList<Country> = ArrayList()
    private lateinit var countriesName:ArrayList<String>
    private var selectedAnswer :String=""
    private var selectedBtn :Button?=null
    private var correctAnswer : String = ""
    private var submitted : Boolean = false

    private  var defaultBtnColor : Int = 0
    private  var correctBtnColor : Int = 0
    private  var wrongBtnColor : Int = 0
    private  var selectedBtnColor : Int = 0
    private var nextBtnColor : Int = 0
    private var submitBtnColor : Int = 0

    private val myScope = CoroutineScope(Dispatchers.IO)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        //safeargs
        args = GameFragmentArgs.fromBundle(requireArguments())
        region = args.region
        userName = args.userName

        lifecycleScope.launch {
            viewModel.fetchCountries(region)
            //main thread
            withContext(Dispatchers.Main){
                setUI()
            }
        }

        return inflater.inflate(R.layout.fragment_game, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        _binding = FragmentGameBinding.bind(view)



        //btns
        defaultBtnColor = ContextCompat.getColor(requireContext(), R.color.purple_700)
        correctBtnColor = ContextCompat.getColor(requireContext(), R.color.green_700)
        wrongBtnColor = ContextCompat.getColor(requireContext(), R.color.red_700)
        selectedBtnColor = ContextCompat.getColor(requireContext(), R.color.gray_700)
        nextBtnColor = ContextCompat.getColor(requireContext(), R.color.gray_200)
        submitBtnColor = ContextCompat.getColor(requireContext(), R.color.black)


        _binding.userNameTv.text = userName
        _binding.regionTv.text = region


        submitBtnHandler()
        answerBtnsHandler()

    }

    private fun answerBtnsHandler() {
        // listeners for the buttons
        _binding.option1Btn.setOnClickListener {
            selectedAnswer = _binding.option1Btn.text.toString().lowercase(Locale.ROOT)
            selectedBtn = _binding.option1Btn
            _binding.option1Btn.setBackgroundColor(selectedBtnColor)
            _binding.option2Btn.setBackgroundColor(defaultBtnColor)
            _binding.option3Btn.setBackgroundColor(defaultBtnColor)
        }
        _binding.option2Btn.setOnClickListener {
            selectedAnswer = _binding.option2Btn.text.toString().lowercase(Locale.ROOT)
            selectedBtn = _binding.option2Btn
            _binding.option2Btn.setBackgroundColor(selectedBtnColor)
            _binding.option1Btn.setBackgroundColor( defaultBtnColor)
            _binding.option3Btn.setBackgroundColor(defaultBtnColor)
        }
        _binding.option3Btn.setOnClickListener {
            selectedAnswer = _binding.option3Btn.text.toString().lowercase(Locale.ROOT)
            selectedBtn = _binding.option3Btn
            _binding.option3Btn.setBackgroundColor(selectedBtnColor)
            _binding.option1Btn.setBackgroundColor(defaultBtnColor)
            _binding.option2Btn.setBackgroundColor(defaultBtnColor)
        }
    }

    private fun submitBtnHandler() {
        _binding.submitBtn.setOnClickListener {
            if (submitted) {
                if (viewModel.currentPosition < questions.size) {
                    setUI()
                    submitted = false
                    _binding.submitBtn.apply {
                        setBackgroundColor(submitBtnColor)
                        setTextColor(nextBtnColor)
                        text = getString(R.string.submit)
                    }
                } else{
                    val action =
                        GameFragmentDirections.actionGameFragmentToResultFragment(viewModel.score, userName)
                    navController.navigate(action)
                    onFinishGame()
                    Toast.makeText(requireContext(), getString(R.string.game_over), Toast.LENGTH_SHORT).show()
                }
            } else {
                if (selectedAnswer.isNotEmpty()) {
                    checkAnswer()
                    submitted = true

                    _binding.submitBtn.apply{
                        text = getString(R.string.next_flag)
                        setBackgroundColor(nextBtnColor)
                        setTextColor(submitBtnColor)
                    }
                } else Toast.makeText(
                    requireContext(),
                    getString(R.string.select_answer),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun onFinishGame(){
        myScope.launch {
            val newUserScore = UserScore(0, userName, viewModel.score)
            context?.let {
                val database : AppDatabase by lazy { Room.databaseBuilder(it, AppDatabase::class.java,"my-database").build()}
                database.userScoreDao().insertUserScore(newUserScore)
            }
        }
    }



    private fun setUI() {
        val question: Question = questions[viewModel.currentPosition]
        Picasso.get().load(question.image).into(_binding.flagIv)

        _binding.option1Btn.apply {
            isEnabled = true
            text = question.options[0]
            setBackgroundColor(defaultBtnColor)

        }
        _binding.option2Btn.apply {
            isEnabled = true
            text = question.options[1]
            setBackgroundColor(defaultBtnColor)
        }
        _binding.option3Btn.apply {
            isEnabled = true
            text = question.options[2]
            setBackgroundColor(defaultBtnColor)
        }

        correctAnswer = question.answer
        viewModel.incrementPosition()

        _binding.questionNumberTv.text = "${viewModel.currentPosition}/${questions.size} flags"
    }


    private fun checkAnswer(){
        if(selectedAnswer == correctAnswer.lowercase(Locale.ROOT)){
            viewModel.incrementScore()
            _binding.score = viewModel.score
            Toast.makeText(requireContext(), "Correct Answer", Toast.LENGTH_SHORT).show()
            selectedBtn?.setBackgroundColor(correctBtnColor)
        }
        else{
            Toast.makeText(requireContext(), "Wrong Answer", Toast.LENGTH_SHORT).show()
            selectedBtn?.setBackgroundColor(wrongBtnColor)
        }
        selectedAnswer = ""
        disableBtns()
    }


    private fun disableBtns(){
        _binding.option3Btn.isEnabled = false
        _binding.option1Btn.isEnabled = false
        _binding.option2Btn.isEnabled = false
    }




}