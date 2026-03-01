package com.example.spacex.data

import com.example.spacex.data.remote.CrewDto
import java.util.UUID

val CREW_DTO = CrewDto(
    name = "Tested person",
    agency = "NASA",
    image = "https://placehold.co/1024",
    wikipedia = "https://en.wikipedia.org/wiki/Space",
    launches = emptyList(),
    status = "active",
    id = "CA3A63DD-D6D0-4069-BB8E-CC9FB386A869"
)

val CREW_DTO_LIST = List(10, { CREW_DTO.copy(id = UUID.randomUUID().toString()) })