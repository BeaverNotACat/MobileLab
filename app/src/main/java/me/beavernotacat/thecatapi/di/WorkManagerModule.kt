package me.beavernotacat.thecatapi.di

import androidx.work.Configuration
import androidx.hilt.work.HiltWorkerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkManagerModule {
    @Provides
    @Singleton
    fun provideWorkManagerConfiguration(
        workerFactory: HiltWorkerFactory,
    ): Configuration = Configuration.Builder()
        .setWorkerFactory(workerFactory)
        .build()
}
