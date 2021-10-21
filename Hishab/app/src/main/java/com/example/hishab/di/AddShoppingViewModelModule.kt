package com.example.hishab.di

import android.app.Application
import com.example.hishab.adapter.PurchaseItemsAdapter
import com.example.hishab.models.entities.PurchaseHistory
import com.example.hishab.repository.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.assisted.AssistedFactory
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@AssistedFactory
interface ProgressDataFactory {
    fun create(dataSet: List<PurchaseHistory>): PurchaseItemsAdapter
}


@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {

    @Singleton
    @Provides
    fun provideRepository(app: Application):ShoppingRepository
    {
        //val application=app as Application
        return  ShoppingRepository(app)
    }

}