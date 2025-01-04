package com.example.routinetimerclone.data.di

import android.content.Context
import androidx.room.Room
import com.example.routinetimerclone.data.database.EEfortDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): EEfortDatabase {
        return Room.databaseBuilder(
            context,
            EEfortDatabase::class.java,
            "app_database",
        ).build()
    }

    @Provides
    @Singleton
    fun provideRoutineDao(database: EEfortDatabase) = database.routineDao()

    @Provides
    @Singleton
    fun provideTaskDao(database: EEfortDatabase) = database.routineDao()
}
