package com.example.routineTimerClone.data.di

import com.example.routineTimerClone.data.datasource.RoutineDataSource
import com.example.routineTimerClone.data.entitiy.mapper.RoutineModelMapper
import com.example.routineTimerClone.data.entitiy.mapper.TaskModelMapper
import com.example.routineTimerClone.data.repository.RoutineRepository
import com.example.routineTimerClone.data.repository.RoutineRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
