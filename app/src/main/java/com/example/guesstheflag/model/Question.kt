package com.example.guesstheflag.model

data class Question(
    val id: Int,
    val answer: String,
    val image: String,
    val options: List<String>
)