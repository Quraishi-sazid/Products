package com.example.hishab.di

import android.app.Application
import android.content.Context
import com.example.hishab.repository.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {

    @Singleton
    @Provides
    fun provideRepository(app:Application ):ShoppingRepository
    {
        //val application=app as Application
        return  ShoppingRepository(app)
    }

}