package com.example.evvolicatalogue.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.evvolicatalogue.R
import com.example.evvolicatalogue.data.local.entities.ProductEntity
import com.example.evvolicatalogue.utils.Screen
import kotlinx.coroutines.flow.StateFlow


fun getCategoryProductTitle(product: ProductEntity): String {
    return when (AppCompatDelegate.getApplicationLocales()[0]?.language) {
        "tk" -> product.title
        "ru" -> product.titleRu
        else -> product.title
    }
}

fun getCategoryProductType(product: ProductEntity): String {
    return when (AppCompatDelegate.getApplicationLocales()[0]?.language) {
        "tk" -> product.type
        "ru" -> product.typeRu
        else -> product.type
    } ?: product.type.toString()
}

@Composable
fun CategoryProductsScreen(
    navHostController: NavHostController,
    products: StateFlow<PagingData<ProductEntity>>,
    modifier: Modifier = Modifier,
) {
    ProductListDisplay(
        navHostController = navHostController,
        products = products,
        modifier = Modifier.fillMaxSize()
    )


}


@Composable
fun ProductListDisplay(
    navHostController: NavHostController,
    products: StateFlow<PagingData<ProductEntity>>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val lazyPagingItems = products.collectAsLazyPagingItems()

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {

        items(lazyPagingItems.itemCount) {index ->
            val product = lazyPagingItems[index]
            if (product != null) {
                ProductItem(
                    navHostController = navHostController,
                    product = product,
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.padding_medium),
                            vertical = dimensionResource(id = R.dimen.padding_small)
                        )
                )
            }
        }
    }
}


@Composable
fun ProductItem(
    navHostController: NavHostController,
    product: ProductEntity,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = dimensionResource(id = R.dimen.padding_small))
        ) {
            ProductImage(
                product = product,
                onSeeDetailsButtonClicked = {
                    navHostController.navigate(Screen.ProductDetailScreen.route + "/${product.id}")
                },
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            ) {
                ProductInformation(
                    product = product,
                    modifier = Modifier
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
fun ProductImage(
    product: ProductEntity,
    onSeeDetailsButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val imageModel = ImageRequest.Builder(context = LocalContext.current)
        .data(product.imageUrl)
        .crossfade(true)
        .build()

    val imageState = rememberAsyncImagePainter(model = imageModel).state

    Box(
        modifier = modifier.clickable { onSeeDetailsButtonClicked() },
        contentAlignment = Alignment.Center
    ) {
        if (imageState is AsyncImagePainter.State.Error) {
            Icon(
                imageVector = Icons.Rounded.ImageNotSupported,
                contentDescription = getCategoryProductTitle(product)
            )
        } else {
            AsyncImage(
                model = imageModel,
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = getCategoryProductTitle(product),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun ProductInformation(
    product: ProductEntity,
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = getCategoryProductTitle(product),
                style = MaterialTheme.typography.titleLarge,
            )
            if (product.type != null) {
                Text(
                    text = getCategoryProductType(product),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}





