package com.example.evvolicatalogue.di

import android.content.Context
import androidx.room.Room
import com.example.evvolicatalogue.data.local.database.DatabaseInitializer
import com.example.evvolicatalogue.data.local.database.EvvoliCatalogueDatabase
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
    fun provideDatabase(
        app: Context
    ): EvvoliCatalogueDatabase {
        return Room.databaseBuilder(
            app,
            EvvoliCatalogueDatabase::class.java,
            "evvoli_catalogue.db"
        )
            .addCallback(DatabaseInitializer(app, EvvoliCatalogueDatabase::class.java))
            .build()
    }

    @Provides
    fun provideCategoryDao(database: EvvoliCatalogueDatabase) = database.categoryDao

    @Provides
    fun provideProductDao(database: EvvoliCatalogueDatabase) = database.productDao

    @Provides
    fun provideProductImageDao(database: EvvoliCatalogueDatabase) = database.productImageDao
}
