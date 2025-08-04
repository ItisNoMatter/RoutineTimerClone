package jp.itIsNoMatter.routineTimerClone.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.itIsNoMatter.routineTimerClone.data.dao.RoutineDao
import jp.itIsNoMatter.routineTimerClone.data.datasource.RoutineDataSource
import jp.itIsNoMatter.routineTimerClone.data.datasource.RoutineLocalDataSource
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
