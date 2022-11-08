package com.e.birra

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET(value = "breweries")
    suspend fun getBreweryList(): Response<MutableList<Birra>>
}