package me.beavernotacat.thecatapi.models

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.runTest
import me.beavernotacat.thecatapi.MainDispatcherRule
import me.beavernotacat.thecatapi.models.dao.PatDao
import me.beavernotacat.thecatapi.models.local.CatPatState
import me.beavernotacat.thecatapi.models.local.PatState
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PatDaoTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var database: CatDataBase
    private lateinit var patDao: PatDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, CatDataBase::class.java)
            .setTransactionExecutor(mainDispatcherRule.dispatcher.asExecutor())
            .setQueryExecutor(mainDispatcherRule.dispatcher.asExecutor())
            .allowMainThreadQueries()
            .build()
        patDao = database.patDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun `re-adding existing cat moves it to top by updatedAt`() = runTest {
        patDao.replacePatState(PatState(id = "cat-a", patState = CatPatState.Patted, updatedAt = 1))
        patDao.replacePatState(PatState(id = "cat-b", patState = CatPatState.Patted, updatedAt = 2))
        patDao.replacePatState(PatState(id = "cat-c", patState = CatPatState.Patted, updatedAt = 3))

        patDao.replacePatState(PatState(id = "cat-a", patState = CatPatState.Patted, updatedAt = 4))

        val ordered = patDao.getByState(CatPatState.Patted).map { it.id }
        assertEquals(listOf("cat-a", "cat-c", "cat-b"), ordered)
    }
}

