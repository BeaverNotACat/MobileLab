package com.example.spacex.data

import com.example.spacex.NetworkModule
import com.example.spacex.data.remote.CrewDto
import com.example.spacex.data.remote.SpaceXApi

class SpaceXRepository(private val api: SpaceXApi = NetworkModule.api) {
    suspend fun listCrews(): List<CrewDto> {
        return api.list()
    }

    suspend fun loadCrew(name: String): CrewDto {
        return api.load(name)
    }
}