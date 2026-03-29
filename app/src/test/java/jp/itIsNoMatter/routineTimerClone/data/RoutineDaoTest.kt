package jp.itIsNoMatter.routineTimerClone.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.itIsNoMatter.routineTimerClone.data.dao.RoutineDao
import jp.itIsNoMatter.routineTimerClone.data.database.EEffortDatabase
import jp.itIsNoMatter.routineTimerClone.data.entity.RoutineEntity
import jp.itIsNoMatter.routineTimerClone.data.entity.TaskEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class RoutineDaoTest {
    private lateinit var database: EEffortDatabase
    private lateinit var routineDao: RoutineDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // テストごとにクリーンな状態にするため、インメモリDBを使用
        database = Room.inMemoryDatabaseBuilder(context, EEffortDatabase::class.java).build()
        routineDao = database.routineDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetRoutine() =
        runTest {
            // 1. テストデータの準備 (UUIDはEntity側で自動生成されるはず)
            val routine = RoutineEntity(name = "Test Routine")
            val routineId = routine.id

            // 2. 保存
            routineDao.insertRoutine(routine)

            // 3. 検証 (Flowなのでfirst()で値を取得)
            val result = routineDao.getRoutineById(routineId).first()

            assertThat(result?.routine?.id, `is`(routineId))
            assertThat(result?.routine?.name, `is`("Test Routine"))
        }

    @Test
    fun insertRoutineWithTasks_ShouldSaveBoth() =
        runTest {
            // 1. 親と子の準備
            val routine = RoutineEntity(name = "Routine with Tasks")
            val tasks =
                listOf(
                    TaskEntity(
                        name = "Task 1",
                        seconds = 60,
                        id = "1",
                        parentRoutineId = "1",
                        announceRemainingTimeFlag = true,
                    ),
                    TaskEntity(
                        name = "Task 2",
                        seconds = 60,
                        id = "2",
                        parentRoutineId = "2",
                        announceRemainingTimeFlag = true,
                    ),
                )

            // 2. トランザクション実行
            val returnedId = routineDao.insertRoutineWithTasks(routine, tasks)

            // 3. 検証
            val result = routineDao.getRoutineById(returnedId).first()

            assertThat(result, notNullValue())
            assertThat(result?.routine?.id, `is`(routine.id))
            assertThat(result?.tasks?.size, `is`(2))
            assertThat(result?.tasks?.get(0)?.parentRoutineId, `is`(routine.id))
        }

    @Test
    fun updateRoutineWithTasks_ShouldReplaceOldTasks() =
        runTest {
            // 1. 初期データ作成
            val routine = RoutineEntity(name = "Original")
            val oldTasks =
                listOf(
                    TaskEntity(
                        name = "Old Task",
                        seconds = 10,
                        id = "1",
                        parentRoutineId = "1",
                        announceRemainingTimeFlag = true,
                    ),
                )
            routineDao.insertRoutineWithTasks(routine, oldTasks)

            // 2. 更新用データ (タスクを入れ替える)
            val newTasks =
                listOf(
                    TaskEntity(
                        name = "New Task 1",
                        seconds = 100,
                        id = "1",
                        parentRoutineId = "1",
                        announceRemainingTimeFlag = true,
                    ),
                    TaskEntity(
                        name = "New Task 2",
                        seconds = 200,
                        id = "2",
                        parentRoutineId = "2",
                        announceRemainingTimeFlag = true,
                    ),
                )
            val updatedRoutine = routine.copy(name = "Updated Name")

            // 3. アップデート実行
            routineDao.updateRoutineWithTasks(updatedRoutine, newTasks)

            // 4. 検証
            val result = routineDao.getRoutineById(routine.id).first()

            assertThat(result?.routine?.name, `is`("Updated Name"))
            assertThat(result?.tasks?.size, `is`(2))
            assertThat(result?.tasks?.any { it.name == "Old Task" }, `is`(false))
            assertThat(result?.tasks?.any { it.name == "New Task 1" }, `is`(true))
        }

    @Test
    fun deleteRoutine_ShouldRemoveRoutine() =
        runTest {
            val routine = RoutineEntity(name = "To Delete")
            routineDao.insertRoutine(routine)

            routineDao.deleteRoutineById(routine.id)

            val result = routineDao.getRoutineById(routine.id).first()
            assertThat(result, nullValue())
        }
}
