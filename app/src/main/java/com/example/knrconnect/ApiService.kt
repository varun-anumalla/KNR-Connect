package com.example.knrconnect

import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    suspend fun getBusinesses(@Url url: String): List<Business>
}