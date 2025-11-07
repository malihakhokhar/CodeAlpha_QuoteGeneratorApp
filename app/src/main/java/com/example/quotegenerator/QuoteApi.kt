package com.example.quotegenerator

import retrofit2.Call
import retrofit2.http.GET

interface QuoteApi {

    @GET("quotes")
    fun getQuotes(): Call<List<QuoteGeneratorItem>>

}