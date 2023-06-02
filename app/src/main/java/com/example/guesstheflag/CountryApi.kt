package com.example.guesstheflag

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryApi {

    @GET("all")
    suspend fun getAllCountries(): Response<List<Country>>

    // get countries by region
    @GET("region/{region}")
    suspend fun getCountriesByRegion(@Path("region")region:String): Response<List<Country>>


}