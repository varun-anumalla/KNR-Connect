package com.example.knrconnect

import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Defines the API endpoints for the application using Retrofit.
 * This interface describes the network requests that can be made.
 */

interface ApiService {

    /**
     * Fetches the list of all businesses from a URL.
     * This is a suspend function, so it must be called from a coroutine.
     *
     * @param url The full URL of the JSON data source (e.g., from a GitHub Gist).
     * @return A list of [Business] objects parsed from the JSON response.
     */

    @GET
    suspend fun getBusinesses(@Url url: String): List<Business>
}