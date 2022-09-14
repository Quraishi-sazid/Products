package com.example.hishab.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.hishab.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Singleton



/*@EntryPoint
@InstallIn(SingletonComponent::class)
interface FooEntryPoint {
    val database: AppDatabase
}*/


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext app: Context):AppDatabase
    {
        synchronized(this) {
            return AppDatabase.INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    app,
                    AppDatabase::class.java,
                    "hishab_database3"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        GlobalScope.launch {
                            AppDatabase.insertPredefinedData()
                        }
                    }
                }).fallbackToDestructiveMigration()
                    .build()
                AppDatabase.INSTANCE = instance
                instance
            }
        }
    }
}

