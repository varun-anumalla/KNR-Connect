package com.example.knrconnect

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A singleton object that provides a ready-to-use instance of the Retrofit API service.
 * This ensures that only one instance of Retrofit is created for the entire application,
 * which is an efficient and standard practice.
 */

object RetrofitInstance {

    /**
     * Lazily creates and configures the Retrofit instance for the [ApiService].
     * The base URL is set to the location of the Gist data, and Gson is used for JSON conversion.
     */

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://gist.githubusercontent.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}