package com.example.hishab.di

import android.app.Application
import com.example.hishab.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton




@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {
    @Singleton
    @Provides
    fun provideRepository(app: Application):Repository
    {
        return  Repository(app)
    }
}