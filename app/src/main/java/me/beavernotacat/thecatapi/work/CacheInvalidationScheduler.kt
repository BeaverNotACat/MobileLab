package me.beavernotacat.thecatapi.work

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import me.beavernotacat.thecatapi.repositories.SettingsRepository

interface CacheInvalidationScheduler {
    fun schedule()
}

@Singleton
class DefaultCacheInvalidationScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
) : CacheInvalidationScheduler {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun schedule() {
        scope.launch {
            val ttlMinutes = settingsRepository.getCacheTtl()
            val intervalMinutes = ttlMinutes.coerceAtLeast(MIN_PERIODIC_INTERVAL_MINUTES)
            val request = PeriodicWorkRequestBuilder<CacheInvalidationWorker>(
                intervalMinutes,
                TimeUnit.MINUTES,
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                request,
            )
        }
    }

    companion object {
        const val WORK_NAME = "cache_invalidation"
        private const val MIN_PERIODIC_INTERVAL_MINUTES = 15L
    }
}
