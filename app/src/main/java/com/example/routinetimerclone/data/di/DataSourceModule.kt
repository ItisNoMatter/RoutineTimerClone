package com.example.routinetimerclone.data.di

import com.example.routinetimerclone.data.dao.RoutineDao
import com.example.routinetimerclone.data.datasource.RoutineDataSource
import com.example.routinetimerclone.data.datasource.RoutineLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideRoutineDataSource(routineDao: RoutineDao): RoutineDataSource {
        return RoutineLocalDataSource(routineDao)
    }
}
