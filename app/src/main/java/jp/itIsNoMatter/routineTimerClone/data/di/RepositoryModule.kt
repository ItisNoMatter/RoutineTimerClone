package jp.itIsNoMatter.routineTimerClone.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.itIsNoMatter.routineTimerClone.data.datasource.RoutineDataSource
import jp.itIsNoMatter.routineTimerClone.data.entitiy.mapper.RoutineModelMapper
import jp.itIsNoMatter.routineTimerClone.data.entitiy.mapper.TaskModelMapper
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepository
import jp.itIsNoMatter.routineTimerClone.data.repository.RoutineRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRoutineRepository(routineDataSource: RoutineDataSource): RoutineRepository {
        return RoutineRepositoryImpl(
            dataSource = routineDataSource,
            routineModelMapper = RoutineModelMapper,
            taskModelMapper = TaskModelMapper,
        )
    }
}
