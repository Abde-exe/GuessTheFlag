package com.example.guesstheflag.viewmodel

import androidx.lifecycle.*
import com.example.guesstheflag.Country
import com.example.guesstheflag.model.Question
import com.example.guesstheflag.network.RetrofitInstance
import java.util.*
import kotlin.collections.ArrayList

class GameViewModel  : ViewModel(){

    private var _score: Int = 0
    val score: Int
        get() = _score

    // Position actuelle dans les dans la liste des questions
    private var _currentPosition: Int = 0
    val currentPosition: Int
        get() = _currentPosition


    private  var _countries = ArrayList<Country>()
    private val countries : ArrayList<Country>
        get() = _countries

    // Liste des noms de pays pour générer les choix de réponses aux questions
    private val _countriesName = ArrayList<String>()

    private val _questions = MutableLiveData<List<Question>>()
    val questions : LiveData<List<Question>>
        get() = _questions

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading






    fun incrementScore() {
        _score = (_score ?: 0) + 1
    }

    // Incrémente la position actuelle dans le jeu pour passer à la question suivante
    fun incrementPosition() {
        _currentPosition = (_currentPosition ?: 0) + 1
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

    // Génère les questions du jeu
    private fun setQuestions() {
        val currentQuestions = _questions.value.orEmpty().toMutableList()
        val random = Random()
        val remainingCountries = ArrayList(_countries)

        // Génère 10 questions aléatoires à partir de la liste des pays
        for (i in 0..9) {
            val randomIndex = random.nextInt(remainingCountries.size)
            val country = remainingCountries[randomIndex]

            // Supprime le pays de la liste des noms de pays pour ne pas avoir la bonne réponse en doublon
            _countriesName.remove(country.name.common)
            // Génère 2 choix de réponses aléatoires
            val randomAnswer1 = _countriesName[random.nextInt(_countriesName.size)]
            val randomAnswer2 = _countriesName[random.nextInt(_countriesName.size)]
            // Mélange les choix de réponses et ajoute le nom du pays (bonne réponse)
            val answerOptions: List<String> = listOf(randomAnswer1, randomAnswer2, country.name.common).shuffled()

            val question = Question(i, country.name.common, country.flags.png, answerOptions)
            currentQuestions.add(question)
            // Supprime le pays de la liste des pays pour ne pas avoir de doublons dans les questions
            remainingCountries.removeAt(randomIndex)
        }

        _questions.value = currentQuestions
    }
}