package com.example.evvolicatalogue.di

import android.content.Context
import androidx.room.Room
import com.example.evvolicatalogue.data.local.database.DatabaseInitializer
import com.example.evvolicatalogue.data.local.database.EvvoliCatalogueDatabase
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
        @ApplicationContext context: Context
    ): EvvoliCatalogueDatabase {
        return Room.databaseBuilder(
            context,
            EvvoliCatalogueDatabase::class.java,
            "evvoli_catalogue.db"
        )
            .addCallback(DatabaseInitializer(context))
            .build()
    }

    @Provides
    fun provideCategoryDao(database: EvvoliCatalogueDatabase) = database.categoryDao

    @Provides
    fun provideProductDao(database: EvvoliCatalogueDatabase) = database.productDao

    @Provides
    fun provideProductImageDao(database: EvvoliCatalogueDatabase) = database.productImageDao
}

