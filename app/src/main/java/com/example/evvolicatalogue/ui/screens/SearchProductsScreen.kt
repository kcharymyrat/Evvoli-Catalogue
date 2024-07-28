package com.example.evvolicatalogue.ui.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.evvolicatalogue.R
import com.example.evvolicatalogue.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SearchProductsScreen(
    navHostController: NavHostController,
    products: StateFlow<PagingData<ProductEntity>>,
    modifier: Modifier = Modifier,
) {
    println("products = $products")
    SearchProductListDisplay(
        navHostController = navHostController,
        products = products,
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
fun SearchProductListDisplay(
    navHostController: NavHostController,
    products: StateFlow<PagingData<ProductEntity>>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val lazyPagingItems = products.collectAsLazyPagingItems()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier,
    ) {

        items(lazyPagingItems.itemCount) {index ->
            val product = lazyPagingItems[index]
            println("product = $product")
            if (product != null) {
                ProductItem(
                    navHostController = navHostController,
                    product = product,
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.padding_small),
                            vertical = dimensionResource(id = R.dimen.padding_small)
                        )
                )
            }
        }
    }
}