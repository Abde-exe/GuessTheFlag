package com.example.guesstheflag.viewmodel

import androidx.lifecycle.*
import com.example.guesstheflag.Country
import com.example.guesstheflag.model.Question
import com.example.guesstheflag.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class GameViewModel  : ViewModel(){

    private var _score: Int = 0
    val score: Int
        get() = _score

    private var _currentPosition: Int = 0
    val currentPosition: Int
        get() = _currentPosition


    private  var _countries = ArrayList<Country>()
    private val countries : ArrayList<Country>
        get() = _countries

    private  var _countriesName:ArrayList<String> = ArrayList()

    private  var _questions = MutableLiveData<List<Question>>()
    val questions : LiveData<List<Question>>
        get() = _questions

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading






    fun incrementScore() {
        _score++
    }

    fun incrementPosition() {
        _currentPosition++
    }

    suspend fun fetchCountries(region:String){
        _isLoading.value = true


            val response = try {
                if(region=="Worldwide") RetrofitInstance.api.getAllCountries()
                else RetrofitInstance.api.getCountriesByRegion(region)
            }
            catch (e:Exception){
                println("Error: ${e.message}")
                null
            }
            if (response != null) {
                if (response.isSuccessful && response.body() != null){
                    for (jsonObject in response.body()!!)
                    {
                        val country = Country(jsonObject.name, jsonObject.region, jsonObject.flags)
                        _countries.add(country)
                        _countriesName.add(country.name.common)
                    }

                }
                setQuestions()
            }

        _isLoading.value = false
    }

     private suspend fun setQuestions(){
         val currentQuestions = _questions.value ?: emptyList()
         val newQuestions =  currentQuestions.toMutableList()
         val newCountries  = _countries

        val random = Random()
        for (i in 0..9){
            val randomIndex = random.nextInt(newCountries.size)
            val country = newCountries[randomIndex]
            _countriesName.filter { it != country.name.common}
            val randomAnswer1 = _countriesName[random.nextInt(_countriesName.size)]
            val randomAnswer2 = _countriesName[random.nextInt(_countriesName.size)]
            val answerOptions : List<String> = listOf(randomAnswer1, randomAnswer2, country.name.common).shuffled()

            val question = Question(i, country.name.common, country.flags.png,answerOptions )
            newQuestions.add(question)
            newCountries.removeAt(randomIndex)
        }
         _questions.value =  newQuestions
    }
}