package com.example.routineTimerClone.data.di

import com.example.routineTimerClone.data.dao.RoutineDao
import com.example.routineTimerClone.data.datasource.RoutineDataSource
import com.example.routineTimerClone.data.datasource.RoutineLocalDataSource
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
