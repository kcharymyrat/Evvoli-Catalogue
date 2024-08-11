package com.example.evvolicatalogue

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.evvolicatalogue.ui.EvvoliCatalogueScreensContainer
import com.example.evvolicatalogue.ui.theme.EvvoliCatalogueTheme
import com.example.evvolicatalogue.utils.Screen
import com.example.evvolicatalogue.viewmodel.CategoryViewModel
import com.example.evvolicatalogue.viewmodel.ProductImageViewModel
import com.example.evvolicatalogue.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EvvoliCatalogueTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val categoryViewModel = hiltViewModel<CategoryViewModel>()
                    val productViewModel = hiltViewModel<ProductViewModel>()
                    val productImageViewModel = hiltViewModel<ProductImageViewModel>()
                    val navHostController: NavHostController = rememberNavController()

                    val startDestination = Screen.CategoriesScreen.route

                    EvvoliCatalogueScreensContainer(
                        categoryViewModel = categoryViewModel,
                        productViewModel = productViewModel,
                        productImageViewModel = productImageViewModel,
                        navHostController = navHostController,
                        startDestination = startDestination
                    )

                }
            }
        }
    }
}


