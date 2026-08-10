package me.beavernotacat.thecatapi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import me.beavernotacat.thecatapi.repositories.SettingsDefaults
import me.beavernotacat.thecatapi.repositories.SettingsRepository

class FakeSettingsRepository : SettingsRepository {
    private val ttlMinutes = MutableStateFlow(SettingsDefaults.DEFAULT_CACHE_TTL)
    override val cacheTtlFlow: Flow<Long> = ttlMinutes

    override suspend fun getCacheTtl(): Long = ttlMinutes.value

    override suspend fun setCacheTtl(minutes: Long) {
        ttlMinutes.value = minutes.coerceAtLeast(1)
    }
}
