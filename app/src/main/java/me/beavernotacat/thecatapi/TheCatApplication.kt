package me.beavernotacat.thecatapi

import android.app.Application
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject
import me.beavernotacat.thecatapi.work.CacheInvalidationScheduler

@HiltAndroidApp
class TheCatApplication : Application(), Configuration.Provider {
    @Inject lateinit var workerConfiguration: Configuration
    @Inject lateinit var cacheInvalidationScheduler: CacheInvalidationScheduler

    override val workManagerConfiguration: Configuration
        get() = workerConfiguration

    override fun onCreate() {
        super.onCreate()
        cacheInvalidationScheduler.schedule()
    }
}