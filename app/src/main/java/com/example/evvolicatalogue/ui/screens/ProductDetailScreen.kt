package com.example.evvolicatalogue.ui.screens

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.evvolicatalogue.R
import com.example.evvolicatalogue.data.local.entities.ProductWithImages
import com.example.evvolicatalogue.utils.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.roundToInt

fun getProductDetailTitle(productDetail: ProductWithImages): String {
    return when (AppCompatDelegate.getApplicationLocales()[0]?.language) {
        "tk" -> productDetail.product.title
        "ru" -> productDetail.product.titleRu
        else -> productDetail.product.title
    }
}

fun getProductDetailDescription(productDetail: ProductWithImages): String {
    return when (AppCompatDelegate.getApplicationLocales()[0]?.language) {
        "tk" -> productDetail.product.description
        "ru" -> productDetail.product.descriptionRu
        else -> productDetail.product.description
    } ?: productDetail.product.description.toString()
}

fun getProductDetailType(productDetail: ProductWithImages): String {
    return when (AppCompatDelegate.getApplicationLocales()[0]?.language) {
        "tk" -> productDetail.product.type
        "ru" -> productDetail.product.typeRu
        else -> productDetail.product.type
    } ?: productDetail.product.type.toString()
}


@Composable
fun ProductDetailScreen(
    navHostController: NavHostController,
    product: StateFlow<ProductWithImages?>,
    modifier: Modifier = Modifier
) {

    var showRedirectButton by remember {
        mutableStateOf(false)
    }

    val productDetail: ProductWithImages? = product.collectAsState().value

    // Get screen width
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // Determine the total number of items
    var totalItems = productDetail?.images?.size ?: 0
    if (productDetail?.product?.imageUrl != null) totalItems++

    // State to track the current visible item index
    val listState = rememberLazyListState()
    val currentItemIndex = remember { mutableIntStateOf(0) }

    // Update the current item index based on the LazyRow scroll state
    LaunchedEffect(listState.firstVisibleItemIndex) {
        currentItemIndex.intValue = listState.firstVisibleItemIndex
    }

    // Handle the delayed action separately
    LaunchedEffect(key1 = Unit) {
        delay(5000) // Delay of 5 seconds
        showRedirectButton = true
    }

    if (productDetail == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (!showRedirectButton) {
                CircularProgressIndicator()
            } else {
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
                        text = "Seem that there is an error with the this product!",
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(onClick = { navHostController.navigate(Screen.CategoriesScreen.route)}) {
                        Text("Home Screen")
                    }
                }
            }
        }
    } else {

        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(8.dp),
        ) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White,
                        disabledContainerColor = Color.White,
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .height(350.dp)
                ) {
                    LazyRow(
                        state = listState,
                        // You can customize the content padding if needed
                        contentPadding = PaddingValues(0.dp),
                        // Customize the horizontal arrangement of items
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {


                        item {
                            if (productDetail.product.imageUrl.isNotBlank()) {
                                ProductDetailImage(
                                    productDetail = productDetail,
                                    imageUrl = productDetail.product.imageUrl,
                                    modifier = Modifier
                                        .width(screenWidth) // Full screen width
                                        .height(300.dp)
                                )
                            }
                        }

                        items(items = productDetail.images) { image ->
                            // Replace this with your custom composable item
                            val imageUrl = image.imageUrl
                            ProductDetailImage(
                                productDetail = productDetail,
                                imageUrl = imageUrl,
                                modifier = Modifier
                                    .width(screenWidth)
                                    .height(300.dp)
                            )
                        }

                    }
                    // Dot indicators
                    DotIndicators(totalItems = totalItems, currentIndex = currentItemIndex.intValue)
                }
            }

            item {
                Column(
                    modifier = modifier
                        .padding(top = dimensionResource(id = R.dimen.padding_medium))
                ) {
                    ProductDetailInformation(
                        productDetail = productDetail,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
                    )
                }
            }
        }
    }
}


@Composable
fun ProductDetailImage(
    productDetail: ProductWithImages,
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    var isFullscreen by remember { mutableStateOf(false) }

    val imageModel = ImageRequest.Builder(context = LocalContext.current)
        .data(imageUrl)
        .crossfade(true)
        .build()

    val imageState = rememberAsyncImagePainter(model = imageModel).state

    Box(
        modifier = modifier.clickable { isFullscreen = true },
        contentAlignment = Alignment.Center
    ) {
        if (imageState is AsyncImagePainter.State.Error) {
            Icon(
                imageVector = Icons.Rounded.ImageNotSupported,
                contentDescription = getProductDetailTitle(productDetail)
            )
        } else {
            AsyncImage(
                model = imageModel,
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = getProductDetailTitle(productDetail),
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (isFullscreen) {
        FullscreenImageDialog(
            imageModel = imageModel,
            contentDescription = getProductDetailTitle(productDetail),
            onDismiss = { isFullscreen = false }
        )
    }
}

@Composable
fun FullscreenImageDialog(
    imageModel: ImageRequest,
    contentDescription: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp)) // Apply rounded corners to the background
                .background(Color.Black)
        ) {
            ZoomableImage(
                imageModel = imageModel,
                contentDescription = contentDescription,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onDismiss() }
            )
        }
    }
}

@Composable
fun ZoomableImage(
    modifier: Modifier = Modifier,
    imageModel: ImageRequest,
    contentDescription: String
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val imageState = rememberAsyncImagePainter(model = imageModel).state

    Box(
        modifier = modifier
            .fillMaxSize()
            .scale(scale)
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, zoom, _ ->
                    scale *= zoom
                    offset = Offset(
                        x = offset.x + pan.x,
                        y = offset.y + pan.y
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        if (imageState is AsyncImagePainter.State.Error) {
            Icon(
                imageVector = Icons.Rounded.ImageNotSupported,
                contentDescription = contentDescription
            )
        } else {
            AsyncImage(
                model = imageModel,
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = contentDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}



@Composable
fun ProductDetailInformation(
    productDetail: ProductWithImages,
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
                text = getProductDetailTitle(productDetail),
                style = MaterialTheme.typography.titleLarge,
            )
            if (productDetail.product.type != null) {
                Text(
                    text = getProductDetailType(productDetail),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = getProductDetailDescription(productDetail),
            color = Color.Gray,
            style = MaterialTheme.typography.labelMedium,
        )


    }
}

@Composable
fun DotIndicators(totalItems: Int, currentIndex: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        for (i in 0 until totalItems) {
            Dot(isSelected = i == currentIndex)
        }
    }
}

@Composable
fun Dot(isSelected: Boolean) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(10.dp)
            .clip(CircleShape)
            .background(if (isSelected) MaterialTheme.colorScheme.tertiary else Color.LightGray)
    )
}
