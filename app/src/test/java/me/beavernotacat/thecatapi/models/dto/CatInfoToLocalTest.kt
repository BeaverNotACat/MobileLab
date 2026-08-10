package me.beavernotacat.thecatapi.models.dto

import me.beavernotacat.thecatapi.data.abys
import me.beavernotacat.thecatapi.data.abysDto
import org.junit.Assert.*
import org.junit.Test

class CatInfoToLocalTest {
    @Test
    fun `catInfo toLocal`() {
        val localCatInfo = abysDto.toLocal()
        assertEquals(abys, localCatInfo)
    }

}