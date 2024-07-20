package com.example.evvolicatalogue.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.evvolitm.R
import com.example.evvolitm.domain.model.CartItemProduct
import com.example.evvolitm.presentation.ProductScreenEvents
import com.example.evvolitm.presentation.ProductScreenState
import com.example.evvolitm.ui.components.NoSearchResultsScreen
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay

@Composable
fun SearchProductsScreen(
    navController: NavHostController,
    searchProductScreenState: ProductScreenState,
    onSearchProductScreenEvent: (ProductScreenEvents, String) -> Unit,
    cartScreenState: CartScreenState,
    onUpdateCartAndItsState: (CartItemProduct, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {

    var isWaitedEnough by remember { mutableStateOf(false) }

    if (searchProductScreenState.productList.isEmpty() && searchProductScreenState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (searchProductScreenState.productList.isEmpty()) {
        NoSearchResultsScreen()
    } else {
        SearchProductListDisplay(
            navController = navController,
            searchProductScreenState = searchProductScreenState,
            onSearchProductScreenEvent = onSearchProductScreenEvent,
            cartScreenState = cartScreenState,
            onUpdateCartAndItsState = onUpdateCartAndItsState,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
fun SearchProductListDisplay(
    navController: NavHostController,
    searchProductScreenState: ProductScreenState,
    onSearchProductScreenEvent: (ProductScreenEvents, String) -> Unit,
    cartScreenState: CartScreenState,
    onUpdateCartAndItsState: (CartItemProduct, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {


        items(searchProductScreenState.productList.size) {productIndex ->

            ProductItem(
                navController = navController,
                product = searchProductScreenState.productList[productIndex],
                cartScreenState = cartScreenState,
                onUpdateCartAndItsState = onUpdateCartAndItsState,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.padding_medium),
                        vertical = dimensionResource(id = R.dimen.padding_small)
                    )
            )

            if (productIndex >= searchProductScreenState.productList.size - 1 && !searchProductScreenState.isLoading) {
                onSearchProductScreenEvent(ProductScreenEvents.OnPaginate(), searchProductScreenState.query)
            }
        }
    }
}