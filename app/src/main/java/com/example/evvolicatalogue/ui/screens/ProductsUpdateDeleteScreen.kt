package com.example.evvolicatalogue.ui.screens

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
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.example.evvolicatalogue.viewmodel.ProductViewModel
import kotlinx.coroutines.flow.StateFlow


@Composable
fun ProductsUpdateDeleteScreen(
    navHostController: NavHostController,
    productViewModel: ProductViewModel,
    products: StateFlow<PagingData<ProductEntity>>,
    modifier: Modifier = Modifier,
) {
    ProductUpdateDeleteList(
        navHostController = navHostController,
        productViewModel = productViewModel,
        products = products,
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
fun ProductUpdateDeleteList(
    navHostController: NavHostController,
    productViewModel: ProductViewModel,
    products: StateFlow<PagingData<ProductEntity>>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val lazyPagingItems = products.collectAsLazyPagingItems()
    var showDialog by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<ProductEntity?>(null) }

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {

        items(lazyPagingItems.itemCount) {index ->
            val product = lazyPagingItems[index]
            if (product != null) {
                ProductUpdateDeleteItem(
                    navHostController = navHostController,
                    product = product,
                    setShowDialog = { show, product ->
                        showDialog = show
                        productToDelete = product
                    },
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.padding_medium),
                            vertical = dimensionResource(id = R.dimen.padding_small)
                        )
                )
            }
        }
    }

    // Confirmation dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.delete))},
            text = { Text(stringResource(R.string.are_you_sure_you_want_to_delete))},
            confirmButton = {
                Button(
                    onClick = {
                        productToDelete?.let {
                            productViewModel.deleteProduct(it)
                        }
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}


@Composable
fun ProductUpdateDeleteItem(
    navHostController: NavHostController,
    setShowDialog: (Boolean, ProductEntity?) -> Unit,
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
            ProductUpdateDeleteImage(
                product = product,
                onSeeDetailsButtonClicked = {
                    navHostController.navigate(
                        Screen.ProductUpdateDeleteScreen.route + "/${product.id}"
                    )
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
            ProductsUpdateDeleteScreenButtons(
                navHostController = navHostController,
                product = product,
                setShowDialog = setShowDialog
            )
        }
    }
}

@Composable
fun ProductUpdateDeleteImage(
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
fun ProductsUpdateDeleteScreenButtons(
    navHostController: NavHostController,
    product: ProductEntity,
    setShowDialog: (Boolean, ProductEntity?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
            onClick = {
                navHostController.navigate(
                    Screen.ProductUpdateDeleteScreen.route + "/${product.id}"
                )
            }
        ) {
            Text(stringResource(R.string.update_product))
        }
        Button(
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            onClick = {
                setShowDialog(true, product) // Show the confirmation dialog
            }
        ) {
            Text(stringResource(R.string.delete_product))
        }
    }
}







