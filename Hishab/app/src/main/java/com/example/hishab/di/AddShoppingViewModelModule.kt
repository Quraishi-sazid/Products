package com.example.hishab.di

import android.app.Application
import android.content.Context
import com.example.hishab.repository.CategoryRepository
import com.example.hishab.repository.PayloadRepository
import com.example.hishab.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun provideCategoryRepository(app: Application):CategoryRepository
    {
        return  CategoryRepository(app)
    }

    @Singleton
    @Provides
    fun providePayLoadRepository(app: Context):PayloadRepository
    {
        return  PayloadRepository(app)
    }
}