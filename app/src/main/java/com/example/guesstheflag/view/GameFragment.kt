package com.example.guesstheflag.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
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
    private  var questions:List<Question> = ArrayList()

    private var selectedAnswer :String=""
    private var selectedBtn :Button?=null
    private var correctAnswer : String = ""
    private var submitted : Boolean = false

    private lateinit var option1btn : Button
    private lateinit var option2btn : Button
    private lateinit var option3btn : Button
    private lateinit var submitBtn : Button
    private lateinit var flagImg : ImageView
    private lateinit var progressBar : ProgressBar

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
        //safeargs
        args = GameFragmentArgs.fromBundle(requireArguments())
        region = args.region
        userName = args.userName

        viewModel = ViewModelProvider(this)[GameViewModel::class.java]
        viewModel.questions.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            questions = viewModel.questions.value as ArrayList<Question>
            setUI()

        })


        lifecycleScope.launch{
            viewModel.fetchCountries(region)
        }


        return inflater.inflate(R.layout.fragment_game, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        _binding = FragmentGameBinding.bind(view)


        //views
        option1btn = _binding.option1Btn
        option2btn = _binding.option2Btn
        option3btn = _binding.option3Btn
        submitBtn = _binding.submitBtn
        flagImg = _binding.flagIv

        //btns colors
        defaultBtnColor = ContextCompat.getColor(requireContext(), R.color.purple_700)
        correctBtnColor = ContextCompat.getColor(requireContext(), R.color.green_700)
        wrongBtnColor = ContextCompat.getColor(requireContext(), R.color.red_700)
        selectedBtnColor = ContextCompat.getColor(requireContext(), R.color.gray_700)
        nextBtnColor = ContextCompat.getColor(requireContext(), R.color.gray_200)
        submitBtnColor = ContextCompat.getColor(requireContext(), R.color.black)


        _binding.userNameTv.text = userName
        _binding.regionTv.text = region

        progressBar = _binding.progressBar

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                showLoadingIndicator()
            } else {
                hideLoadingIndicator()
            }
        }
        submitBtnHandler()
        answerBtnsHandler()

    }

    private fun answerBtnsHandler() {
        // listeners for the buttons
        option1btn.setOnClickListener {
            selectedAnswer = _binding.option1Btn.text.toString().lowercase(Locale.ROOT)
            selectedBtn = _binding.option1Btn
            option1btn.setBackgroundColor(selectedBtnColor)
            option2btn.setBackgroundColor(defaultBtnColor)
            option3btn.setBackgroundColor(defaultBtnColor)
        }
        option2btn.setOnClickListener {
            selectedAnswer = _binding.option2Btn.text.toString().lowercase(Locale.ROOT)
            selectedBtn = _binding.option2Btn
            option2btn.setBackgroundColor(selectedBtnColor)
            option1btn.setBackgroundColor( defaultBtnColor)
            option3btn.setBackgroundColor(defaultBtnColor)
        }
        option3btn.setOnClickListener {
            selectedAnswer = _binding.option3Btn.text.toString().lowercase(Locale.ROOT)
            selectedBtn = _binding.option3Btn
            option3btn.setBackgroundColor(selectedBtnColor)
            option1btn.setBackgroundColor(defaultBtnColor)
            option2btn.setBackgroundColor(defaultBtnColor)
        }
    }

    private fun hideLoadingIndicator() {
        progressBar.visibility = View.GONE
    }

    private fun showLoadingIndicator() {
        progressBar.visibility = View.VISIBLE
    }


    private fun submitBtnHandler() {
       submitBtn.setOnClickListener {
            if (submitted) {
                if (viewModel.currentPosition < questions.size) {
                    setUI()
                    submitted = false
                    submitBtn.apply {
                        setBackgroundColor(submitBtnColor)
                        setTextColor(nextBtnColor)
                        text = getString(R.string.submit)
                    }
                } else{
                    onFinishGame()
                    val action =
                        GameFragmentDirections.actionGameFragmentToResultFragment(viewModel.score, userName)
                    navController.navigate(action)
                    Toast.makeText(requireContext(), getString(R.string.game_over), Toast.LENGTH_SHORT).show()
                }
            } else {
                if (selectedAnswer.isNotEmpty()) {
                    checkAnswer()
                    submitted = true

                    submitBtn.apply{
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
        flagImg.visibility = View.VISIBLE
        Picasso.get().load(question.image).into(_binding.flagIv)

        option1btn.apply {
            visibility = View.VISIBLE
            isEnabled = true
            text = question.options[0]
            setBackgroundColor(defaultBtnColor)

        }
        option2btn.apply {
            visibility = View.VISIBLE
            isEnabled = true
            text = question.options[1]
            setBackgroundColor(defaultBtnColor)
        }
        option3btn.apply {
            visibility = View.VISIBLE
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
        option1btn.isEnabled = false
        option2btn.isEnabled = false
        option3btn.isEnabled = false
    }




}