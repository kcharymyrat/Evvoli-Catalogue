package com.example.evvolicatalogue.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.evvolitm.R
import com.example.evvolitm.data.remote.EvvoliTmApi
import com.example.evvolitm.domain.model.CartItemProduct
import com.example.evvolitm.domain.model.Category
import com.example.evvolitm.domain.model.Product
import com.example.evvolitm.domain.model.ProductDetail
import com.example.evvolitm.presentation.CategoryScreenEvents
import com.example.evvolitm.presentation.ProductScreenEvents
import com.example.evvolitm.presentation.ProductScreenState
import com.example.evvolitm.ui.theme.Shapes
import com.example.evvolitm.util.Screen


fun getCategoryProductTitle(product: Product): String {
    return when (AppCompatDelegate.getApplicationLocales()[0]?.language) {
        "tk" -> product.title
        "ru" -> product.titleRu
        else -> product.titleEn
    }
}

fun getCategoryProductType(product: Product): String {
    return when (AppCompatDelegate.getApplicationLocales()[0]?.language) {
        "tk" -> product.type
        "ru" -> product.typeEn
        else -> product.typeRu
    } ?: product.type.toString()
}

@Composable
fun CategoryProductsScreen(
    navController: NavHostController,
    categoryId: String,
    productScreenState: ProductScreenState,
    onScreenProductEvent: (ProductScreenEvents, String) -> Unit,
    cartScreenState: CartScreenState,
    onUpdateCartAndItsState: (CartItemProduct, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    println("productScreenState.hasError = ${productScreenState.hasError}")
    if (productScreenState.hasError) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_connection_error),
                contentDescription = stringResource(R.string.connection_error),
            )
            Text(
                text = stringResource(R.string.error_loading_data_please_refresh),
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = { onScreenProductEvent(ProductScreenEvents.Refresh, categoryId) }) {
                Text(stringResource(R.string.refresh))
            }
        }
    } else if (productScreenState.productList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        ProductListDisplay(
            navController = navController,
            categoryId = categoryId,
            productScreenState = productScreenState,
            onProductScreenEvent = onScreenProductEvent,
            cartScreenState = cartScreenState,
            onUpdateCartAndItsState = onUpdateCartAndItsState,
            modifier = Modifier.fillMaxSize()
        )
    }


}


@Composable
fun ProductListDisplay(
    navController: NavHostController,
    categoryId: String,
    productScreenState: ProductScreenState,
    onProductScreenEvent: (ProductScreenEvents, String) -> Unit,
    cartScreenState: CartScreenState,
    onUpdateCartAndItsState: (CartItemProduct, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {


        items(productScreenState.productList.size) {productIndex ->

            ProductItem(
                navController = navController,
                product = productScreenState.productList[productIndex],
                cartScreenState = cartScreenState,
                onUpdateCartAndItsState = onUpdateCartAndItsState,
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.padding_medium),
                        vertical = dimensionResource(id = R.dimen.padding_small)
                    )
            )

            if (productIndex >= productScreenState.productList.size - 1 && !productScreenState.isLoading) {
                onProductScreenEvent(ProductScreenEvents.OnPaginate(), categoryId)
            }
        }
    }
}


@Composable
fun ProductItem(
    navController: NavHostController,
    product: Product,
    cartScreenState: CartScreenState,
    onUpdateCartAndItsState: (CartItemProduct, Boolean) -> Unit,
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
                    navController.navigate(Screen.ProductDetailScreen.route + "/${product.id}")
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
                ProductToCartButtons(
                    cartScreenState = cartScreenState,
                    onUpdateCartAndItsState = onUpdateCartAndItsState,
                    product = product
                )
            }
        }
    }
}

@Composable
fun ProductImage(
    product: Product,
    onSeeDetailsButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val imageModel = ImageRequest.Builder(context = LocalContext.current)
        .data(EvvoliTmApi.BASE_URL + product.imageUrl)
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
    product: Product,
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



        if (product.salePrice < product.price) {
            Text(
                text = product.price.toString() + " m.",
                style = MaterialTheme.typography.labelLarge,
                textDecoration = TextDecoration.LineThrough
            )
            Text(
                text = product.salePrice.toString() + " m.",
                style = MaterialTheme.typography.labelLarge,
            )
        } else {
            Text(
                text = product.price.toString() + " m.",
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}


@Composable
fun ProductToCartButtons(
    cartScreenState: CartScreenState,
    onUpdateCartAndItsState: (CartItemProduct, Boolean) -> Unit,
    product: Product,
    modifier: Modifier = Modifier
) {

    val productQty = remember { mutableIntStateOf(0) }
    LaunchedEffect(cartScreenState) {
        productQty.intValue = cartScreenState.cartItems.find {
            it.product.id == product.id
        }?.cartItem?.quantity ?: 0
    }

    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = Shapes.medium
            )
    ) {
        Row(
            modifier = Modifier
        ) {
            if (productQty.intValue > 0) {
                MinusQtyPlus(
                    cartScreenState = cartScreenState,
                    onUpdateCartAndItsState = onUpdateCartAndItsState,
                    product = product,
                    cartProductQty = productQty.intValue,
                    modifier = modifier
                )
            } else {
                ProductAddButton(
                    cartScreenState = cartScreenState,
                    onUpdateCartAndItsState = onUpdateCartAndItsState,
                    product = product,
                    modifier = modifier
                )
            }
        }
    }
}


@Composable
fun ProductAddButton(
    cartScreenState: CartScreenState,
    onUpdateCartAndItsState: (CartItemProduct, Boolean) -> Unit,
    product: Product,
    modifier: Modifier = Modifier
) {
    val newCartItemProduct = CartItemProduct(
        id = product.id,
        title = product.title,
        titleEn = product.titleEn,
        titleRu = product.titleRu,
        model = product.model,
        slug = product.slug,
        imageUrl = product.imageUrl,
        price = product.price.toDouble(),
        salePrice = product.salePrice.toDouble(),
    )
    IconButton(
        onClick = {
            onUpdateCartAndItsState(newCartItemProduct, false)
        },
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = Color.White
        )
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = stringResource(id = R.string.add_to_shopping_cart)
        )
    }
}

@Composable
fun MinusQtyPlus(
    cartScreenState: CartScreenState,
    onUpdateCartAndItsState: (CartItemProduct, Boolean) -> Unit,
    product: Product,
    cartProductQty: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier,
    ) {
        MinusClickable(
            cartScreenState = cartScreenState,
            onUpdateCartAndItsState = onUpdateCartAndItsState,
            product = product
        )

        ProductQty(
            cartScreenState = cartScreenState,
            product = product,
            cartProductQty = cartProductQty
        )

        PlusClickable(
            cartScreenState = cartScreenState,
            onUpdateCartAndItsState = onUpdateCartAndItsState,
            product = product
        )
    }
}

@Composable
fun ProductQty(
    cartScreenState: CartScreenState,
    product: Product,
    cartProductQty: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = cartProductQty.toString(),
        modifier = modifier,
        color = Color.White,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun PlusClickable(
    cartScreenState: CartScreenState,
    onUpdateCartAndItsState: (CartItemProduct, Boolean) -> Unit,
    product: Product,
    modifier: Modifier = Modifier
) {
    val newCartItemProduct = CartItemProduct(
        id = product.id,
        title = product.title,
        titleEn = product.titleEn,
        titleRu = product.titleRu,
        model = product.model,
        slug = product.slug,
        imageUrl = product.imageUrl,
        price = product.price.toDouble(),
        salePrice = product.salePrice.toDouble(),
    )
    IconButton(
        onClick = {
            onUpdateCartAndItsState(newCartItemProduct, false)
        },
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = Color.White
        )
    ) {
        Icon(Icons.Default.Add, contentDescription = "Decrease")
    }
}

@Composable
fun MinusClickable(
    cartScreenState: CartScreenState,
    onUpdateCartAndItsState: (CartItemProduct, Boolean) -> Unit,
    product: Product,
    modifier: Modifier = Modifier
) {
    val newCartItemProduct = CartItemProduct(
        id = product.id,
        title = product.title,
        titleEn = product.titleEn,
        titleRu = product.titleRu,
        model = product.model,
        slug = product.slug,
        imageUrl = product.imageUrl,
        price = product.price.toDouble(),
        salePrice = product.salePrice.toDouble(),
    )
    IconButton(
        onClick = {
            onUpdateCartAndItsState(newCartItemProduct,true)
        },
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = Color.White
        )
    ) {
        Icon(Icons.Default.Remove, contentDescription = "Increase")
    }
}


///**
// * The home screen displaying the loading message.
// */
//@Composable
//fun LoadingScreenProducts(navController: NavHostController, modifier: Modifier = Modifier) {
//    Image(
//        modifier = modifier.size(200.dp),
//        painter = painterResource(R.drawable.loading_img),
//        contentDescription = stringResource(R.string.loading)
//    )
//}
//
///**
// * The home screen displaying error message with re-attempt button.
// */
//@Composable
//fun ErrorScreenProducts(navController: NavHostController, retryAction: () -> Unit, modifier: Modifier = Modifier) {
//    Column(
//        modifier = modifier,
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
//        )
//        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
//        Button(onClick = retryAction) {
//            Text(stringResource(R.string.retry))
//        }
//    }
//}


