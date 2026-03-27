package jp.itIsNoMatter.routineTimerClone.data
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import jp.itIsNoMatter.routineTimerClone.core.LoadedValue
import jp.itIsNoMatter.routineTimerClone.data.datasource.RoutineDataSource
import jp.itIsNoMatter.routineTimerClone.data.entitiy.RoutineEntity
import jp.itIsNoMatter.routineTimerClone.data.entitiy.RoutineWithTasks
import jp.itIsNoMatter.routineTimerClone.data.entitiy.TaskEntity
import jp.itIsNoMatter.routineTimerClone.data.entitiy.mapper.RoutineModelMapper
import jp.itIsNoMatter.routineTimerClone.data.entitiy.mapper.TaskModelMapper
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepositoryImpl
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
    private val dataSource = mockk<RoutineDataSource>()
    private val routineMapper = mockk<RoutineModelMapper>()
    private val taskMapper = mockk<TaskModelMapper>()

    // テスト対象
    private lateinit var repository: RoutineRepositoryImpl

    @Before
    fun setup() {
        repository = RoutineRepositoryImpl(dataSource, routineMapper, taskMapper)
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
            every { dataSource.getRoutineById(routineId) } returns flowOf(realEntity)

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
            every { dataSource.getRoutineById(routineId) } returns flowOf(null)

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
            every { mockMappedResult.routine } returns routineEntity
            every { mockMappedResult.tasks } returns taskEntities

            every { routineMapper.toEntity(routine) } returns mockMappedResult
            coEvery { dataSource.insertRoutineWithTasks(any(), any()) } returns "new-id" // 戻り値は無視されるが定義は必要

            // 実行
            repository.insertRoutine(routine)

            // 検証: 正しい引数でDataSourceのメソッドが1回呼ばれたかを確認
            coVerify(exactly = 1) {
                dataSource.insertRoutineWithTasks(routineEntity, taskEntities)
            }
        }

    @Test
    fun `insertTask - タスクが正しい親IDと共に保存されること`() =
        runTest {
            // 準備
            val task = mockk<Task>()
            val parentId = "parent-uuid"
            val taskEntity = mockk<TaskEntity>()

            every { taskMapper.toEntity(task, parentId) } returns taskEntity
            coEvery { dataSource.insertTask(taskEntity) } returns Unit

            // 実行
            repository.insertTask(task, parentId)

            // 検証
            coVerify(exactly = 1) { dataSource.insertTask(taskEntity) }
        }
}
