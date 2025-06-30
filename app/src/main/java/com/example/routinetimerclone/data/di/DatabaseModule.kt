package com.example.routinetimerclone.data.di

import android.content.Context
import androidx.room.Room
import com.example.routinetimerclone.data.database.EEffortDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): EEffortDatabase {
        return Room.databaseBuilder(
            context,
            EEffortDatabase::class.java,
            "app_database",
        ).build()
    }

    @Provides
    @Singleton
    fun provideRoutineDao(database: EEffortDatabase) = database.routineDao()
}
