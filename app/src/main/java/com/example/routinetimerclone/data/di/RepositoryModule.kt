package com.example.routinetimerclone.data.di

import com.example.routinetimerclone.data.datasource.RoutineDataSource
import com.example.routinetimerclone.data.entitiy.mapper.RoutineModelMapper
import com.example.routinetimerclone.data.entitiy.mapper.TaskModelMapper
import com.example.routinetimerclone.data.repository.RoutineRepository
import com.example.routinetimerclone.data.repository.RoutineRepositoryImpl
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
