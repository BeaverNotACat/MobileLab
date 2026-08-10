package me.beavernotacat.thecatapi.repositories

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton
import me.beavernotacat.thecatapi.work.CacheInvalidationScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private const val SETTINGS_DATASTORE_NAME = "settings"
internal val Context.settingsDataStore by preferencesDataStore(name = SETTINGS_DATASTORE_NAME)

object SettingsDefaults {
    const val DEFAULT_CACHE_TTL: Long = 60 * 60 * 1000
}

interface SettingsRepository {
    val cacheTtlFlow: Flow<Long>
    suspend fun getCacheTtl(): Long
    suspend fun setCacheTtl(millis: Long)
}

@Singleton
class DefaultSettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val cacheInvalidationScheduler: Lazy<CacheInvalidationScheduler>,
) : SettingsRepository {
    private object Keys {
        val CacheTtl: Preferences.Key<Long> = longPreferencesKey("cache_ttl")
    }

    override val cacheTtlFlow: Flow<Long> =
        context.settingsDataStore.data.map { prefs ->
            prefs[Keys.CacheTtl] ?: SettingsDefaults.DEFAULT_CACHE_TTL
        }

    override suspend fun getCacheTtl(): Long {
        return cacheTtlFlow.first()
    }

    override suspend fun setCacheTtl(millis: Long) {
        val validatedMinutes = millis.coerceAtLeast(1)
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.CacheTtl] = validatedMinutes
        }
        cacheInvalidationScheduler.get().schedule()
    }
}
