package com.example.guesstheflag

import com.google.gson.Gson

data class Country(
    val name: CountryName,
    val region: String,
    val flags: FlagUrl
)

data class CountryName(
    val common: String,
    val official: String
)

data class FlagUrl(
    val png: String,
)

