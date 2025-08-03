package com.example.knrconnect

class BusinessRepository {

    private var businessList: List<Business> = emptyList()

    suspend fun getBusinesses(apiUrl: String): List<Business> {
        if (businessList.isEmpty()) {
            businessList = RetrofitInstance.api.getBusinesses(apiUrl)
        }
        return businessList
    }

    fun getBusinessByName(name: String): Business? {
        return businessList.find { it.name == name }
    }
}