package me.beavernotacat.thecatapi.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import me.beavernotacat.thecatapi.repositories.CacheRepository

@HiltWorker
class CacheInvalidationWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val cacheRepository: CacheRepository,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            cacheRepository.invalidateExpiredCache()
            Result.success()
        } catch (_: Exception) {
            Result.retry()
        }
    }
}
