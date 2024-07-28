package com.example.evvolicatalogue.data.local.database

import android.content.Context
import android.util.Log
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
    private val context: Context
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
            EvvoliCatalogueDatabase::class.java,
            "evvoli_catalogue.db"
        ).build()

        val categories = readJsonData<CategoryEntity>(R.raw.categories)
        val products = readJsonData<ProductEntity>(R.raw.products)
        val productImages = readJsonData<ProductImageEntity>(R.raw.product_images)


        database.categoryDao.insertCategoryList(categories)
        val allCategories = database.categoryDao.getCategories()
        Log.d("DatabaseInitializer", "All Categories from DB: $allCategories")
        for (i in 1..12) {
            val cat = database.categoryDao.getCategoryById(i)
            Log.d(
                "DatabaseInitializer",
                "Created Category Entity: ${cat?.id} - ${cat?.name}"
            )
        }

        database.productDao.insertProductList(products)
        val allProducts = database.productDao.getProducts()
        Log.d("DatabaseInitializer", "All Products from DB: $allProducts")
        for (i in 1..57) {
            if (i == 24) {
                continue
            }
            val pr =  database.productDao.getProductById(i)
            Log.d(
                "DatabaseInitializer",
                "Created Product Entity: ${pr.id} - ${pr.title}"
            )
        }

        database.productImageDao.insertProductImageList(productImages)
        val allProductImages = database.productImageDao.getProductImages()
        Log.d(
            "DatabaseInitializer",
            "All ProductImages from DB: $allProductImages"
        )
        for (i in 1..77) {
            val prI = database.productImageDao.getProductImagesByPId(i)
            Log.d(
                "DatabaseInitializer",
                "Created ProductImage Entity: ${prI.id}}"
            )
        }

        Log.d("DatabaseInitializer", "END!")
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




