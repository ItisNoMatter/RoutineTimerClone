package jp.itIsNoMatter.routineTimerClone.data
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.data.local.datasource.RoutineLocalDataSource
import jp.itIsNoMatter.routineTimerClone.data.local.entity.RoutineEntity
import jp.itIsNoMatter.routineTimerClone.data.local.entity.RoutineWithTasks
import jp.itIsNoMatter.routineTimerClone.data.local.entity.TaskEntity
import jp.itIsNoMatter.routineTimerClone.data.local.entity.mapper.RoutineModelMapper
import jp.itIsNoMatter.routineTimerClone.data.local.entity.mapper.TaskModelMapper
import jp.itIsNoMatter.routineTimerClone.data.remote.RoutineResponse
import jp.itIsNoMatter.routineTimerClone.data.remote.datasource.RoutineRemoteDataSource
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepositoryImpl
import jp.itIsNoMatter.routineTimerClone.domain.model.Duration
import jp.itIsNoMatter.routineTimerClone.domain.model.Routine
import jp.itIsNoMatter.routineTimerClone.domain.model.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import java.util.UUID

class RoutineRepositoryTest {
    // モック対象
    private val localDataSource = mockk<RoutineLocalDataSource>()
    private val remoteDataSource = mockk<RoutineRemoteDataSource>()
    private val routineMapper = mockk<RoutineModelMapper>()
    private val taskMapper = mockk<TaskModelMapper>()

    // テスト対象
    private lateinit var repository: RoutineRepositoryImpl

    @Before
    fun setup() {
        repository = RoutineRepositoryImpl(localDataSource, remoteDataSource, routineMapper, taskMapper)
    }

    @Test
    fun `getRoutine - IDを指定して正常に取得できるとき、Doneを返すこと`() =
        runTest {
            // 準備
            val routineId = UUID.randomUUID().toString()

            val routineEntity = RoutineEntity(id = routineId, name = "Test Routine")
            val taskEntities = emptyList<TaskEntity>()
            val realEntity = RoutineWithTasks(routine = routineEntity, tasks = taskEntities)

            val mockDomain = mockk<Routine>()

            // DataSourceが本物のEntityを流すように設定
            every { localDataSource.getRoutineById(routineId) } returns flowOf(realEntity)

            // Mapperにこの実体が渡ってきたら、このモックDomainを返すように設定
            every { routineMapper.toDomain(routineEntity, taskEntities) } returns mockDomain

            // 実行
            val result = repository.getRoutine(routineId).first()

            // 検証
            assertThat(result is LoadedValue.Done, `is`(true))
            assertThat((result as LoadedValue.Done).value, `is`(mockDomain))
        }

    @Test
    fun `getRoutine - 存在しないIDのとき、Errorを返すこと`() =
        runTest {
            // 準備
            val routineId = "non-existent-id"
            every { localDataSource.getRoutineById(routineId) } returns flowOf(null)

            // 実行
            val result = repository.getRoutine(routineId).first()

            // 検証
            assertThat(result is LoadedValue.Error, `is`(true))
        }

    @Test
    fun `insertRoutine - 正しくEntityに変換されてDataSourceが呼ばれること`() =
        runTest {
            // 準備
            val routine = mockk<Routine>()
            val routineEntity = mockk<RoutineEntity>()
            val taskEntities = listOf<TaskEntity>()

            // 1つのRoutineEntityにまとめられるMapperの戻り値を模倣
            // (toEntityの戻り値の型に合わせて調整してください。ここでは(routine, tasks)のPairやラッパークラスを想定)
            val mockMappedResult = mockk<RoutineWithTasks>()
            val mockResponse = mockk<RoutineResponse>()
            every { mockMappedResult.routine } returns routineEntity
            every { mockMappedResult.tasks } returns taskEntities

            every { routineMapper.toEntity(routine) } returns mockMappedResult
            coEvery { localDataSource.insertRoutineWithTasks(any(), any()) } returns "new-id" // 戻り値は無視されるが定義は必要

            every { routineMapper.toResponse(routine) } returns mockResponse
            coEvery { remoteDataSource.addRoutine(any()) } returns Unit

            // 実行
            repository.insertRoutine(routine)

            // 検証: 正しい引数でDataSourceのメソッドが1回呼ばれたかを確認
            coVerify(exactly = 1) {
                localDataSource.insertRoutineWithTasks(routineEntity, taskEntities)
            }
        }

    @Test
    fun `insertTask - タスクが正しい親IDと共に保存されること`() =
        runTest {
            val task = Task(id = "task-1", name = "New Task", duration = Duration(1, 0), announceRemainingTimeFlag = true, orderIndex = 0)
            val parentId = "parent-uuid"
            val taskEntity = mockk<TaskEntity>()

            val existingTasks =
                listOf(
                    TaskEntity(
                        id = "old-1",
                        name = "Old",
                        seconds = 90,
                        parentRoutineId = parentId,
                        orderIndex = 0,
                    ),
                )
            every { localDataSource.getTasksByRoutineId(parentId) } returns flowOf(existingTasks)
            every { taskMapper.toEntity(any(), parentId) } returns taskEntity
            coEvery { localDataSource.insertTask(taskEntity) } returns Unit

            // 2. 実行
            repository.insertTask(task, parentId)

            // 3. 検証
            // getTasksByRoutineId が呼ばれたか
            verify(exactly = 1) { localDataSource.getTasksByRoutineId(parentId) }

            // 最終的に Entity が保存されたか
            coVerify(exactly = 1) { localDataSource.insertTask(taskEntity) }
        }
}
