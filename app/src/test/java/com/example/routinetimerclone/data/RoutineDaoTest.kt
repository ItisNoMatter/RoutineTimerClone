package com.example.routinetimerclone.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.routinetimerclone.data.dao.RoutineDao
import com.example.routinetimerclone.data.database.Database
import com.example.routinetimerclone.data.entitiy.RoutineEntity
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.lang.Thread.sleep

@RunWith(RobolectricTestRunner::class)
class RoutineDaoTest {
private val testDispatcher = StandardTestDispatcher()
private lateinit var db: Database
private lateinit var dao: RoutineDao

@Before
fun setup() {
    db =
        Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), Database::class.java)
            .setQueryExecutor(testDispatcher.asExecutor())
            .setTransactionExecutor(testDispatcher.asExecutor())
            .build()
    dao = db.routineDao()
}

@After
fun close() {
    db.close()
}
@Test
fun insertRoutineTest()= runTest {
    val routine = RoutineEntity(1, "Test Routine")
    val id = dao.insertRoutine(routine)
    assert(routine.id == id)
}
}