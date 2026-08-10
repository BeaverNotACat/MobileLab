package me.beavernotacat.thecatapi

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import me.beavernotacat.thecatapi.repositories.SettingsDefaults
import me.beavernotacat.thecatapi.repositories.SettingsRepository

class FakeSettingsRepository : SettingsRepository {
    private val ttl = MutableStateFlow(SettingsDefaults.DEFAULT_CACHE_TTL)
    override val cacheTtlFlow: Flow<Long> = ttl

    override suspend fun getCacheTtl(): Long = ttl.value

    override suspend fun setCacheTtl(millis: Long) {
        ttl.value = millis.coerceAtLeast(1)
    }
}
