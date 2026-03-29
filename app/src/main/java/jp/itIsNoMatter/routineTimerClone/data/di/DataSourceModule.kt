package jp.itIsNoMatter.routineTimerClone.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.itIsNoMatter.routineTimerClone.data.local.dao.RoutineDao
import jp.itIsNoMatter.routineTimerClone.data.local.datasource.RoutineLocalDataSource
import jp.itIsNoMatter.routineTimerClone.data.local.datasource.RoutineLocalDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideRoutineDataSource(routineDao: RoutineDao): RoutineLocalDataSource {
        return RoutineLocalDataSourceImpl(routineDao)
    }
}
