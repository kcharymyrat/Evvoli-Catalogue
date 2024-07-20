package com.example.evvolicatalogue.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.evvolicatalogue.R
import com.example.evvolicatalogue.data.local.entities.CategoryEntity
import com.example.evvolicatalogue.data.local.entities.ProductEntity
import com.example.evvolicatalogue.data.local.entities.ProductImageEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException



class DatabaseInitializer(
    private val context: Context,
    private val databaseClass: Class<out RoomDatabase>
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        CoroutineScope(Dispatchers.IO).launch {
            populateDatabase()
        }
    }

    private suspend fun populateDatabase() {
        val database = Room.databaseBuilder(
            context,
            databaseClass,
            "evvoli_catalogue.db"
        ).build() as EvvoliCatalogueDatabase

        val categories = readJsonData<CategoryEntity>(R.raw.categories)
        val products = readJsonData<ProductEntity>(R.raw.products)
        val productImages = readJsonData<ProductImageEntity>(R.raw.product_images)

        database.categoryDao.insertCategoryList(categories)
        database.productDao.insertProductList(products)
        database.productImageDao.insertProductImageList(productImages)
    }

    private inline fun <reified T> readJsonData(resId: Int): List<T> {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val type = Types.newParameterizedType(List::class.java, T::class.java)
        val adapter = moshi.adapter<List<T>>(type)

        return try {
            val inputStream = context.resources.openRawResource(resId)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            adapter.fromJson(jsonString) ?: emptyList()
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }
}
