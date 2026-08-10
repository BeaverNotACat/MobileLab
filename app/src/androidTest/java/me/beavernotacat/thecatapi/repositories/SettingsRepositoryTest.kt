package me.beavernotacat.thecatapi.repositories

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.test.core.app.ApplicationProvider
import dagger.Lazy
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import me.beavernotacat.thecatapi.work.CacheInvalidationScheduler
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SettingsRepositoryTest {
    private lateinit var context: Context
    private val noOpScheduler = Lazy<CacheInvalidationScheduler> {
        object : CacheInvalidationScheduler {
            override fun schedule() = Unit
        }
    }

    @Before
    fun setup(): Unit = runBlocking {
        context = ApplicationProvider.getApplicationContext()
        context.settingsDataStore.edit { it.clear() }
    }

    @Test
    fun `default cache ttl is emitted when preference is not set`() = runTest {
        val repository = DefaultSettingsRepository(context, noOpScheduler)

        assertEquals(SettingsDefaults.DEFAULT_CACHE_TTL, repository.getCacheTtl())
    }

    @Test
    fun `cache ttl override persists between repository instances`() = runTest {
        val repository = DefaultSettingsRepository(context, noOpScheduler)
        repository.setCacheTtl(42)

        val reloadedRepository = DefaultSettingsRepository(context, noOpScheduler)

        assertEquals(42, reloadedRepository.getCacheTtl()
        )
    }
}
