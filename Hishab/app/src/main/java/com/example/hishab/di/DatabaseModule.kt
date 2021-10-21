package com.example.hishab.di

import android.app.Application
import androidx.room.Room
import com.example.hishab.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@EntryPoint
@InstallIn(SingletonComponent::class)
interface FooEntryPoint {
    val database: AppDatabase
}


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application):AppDatabase
    {
        synchronized(this) {
            val instance = Room.databaseBuilder(
                app.applicationContext,
                AppDatabase::class.java,
                "hishab_database"
            ).allowMainThreadQueries().
            fallbackToDestructiveMigration().
            build()
            return instance
        }
    }
}
