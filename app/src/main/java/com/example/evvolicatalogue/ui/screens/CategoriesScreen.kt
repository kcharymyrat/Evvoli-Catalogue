package com.example.evvolicatalogue.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest


fun getCategoryName(category: Category): String {
    return when (AppCompatDelegate.getApplicationLocales()[0]?.language) {
        "tk" -> category.name
        "ru" -> category.nameRu
        else -> category.nameEn // default to English
    }
}

@Composable
fun CategoriesScreen(
    navController: NavHostController,
    categoryScreenState: CategoryScreenState,
    onEvent: (CategoryScreenEvents) -> Unit,
    modifier: Modifier = Modifier
) {

    if (categoryScreenState.hasError) {
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
            Button(onClick = { onEvent(CategoryScreenEvents.Refresh) }) {
                Text(stringResource(R.string.refresh))
            }
        }
    } else if (categoryScreenState.categoryList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        CategoryListDisplay(
            navController = navController,
            categoryScreenState = categoryScreenState,
            onEvent = onEvent,
            modifier = Modifier.fillMaxSize()
        )
    }
}


@Composable
fun CategoryListDisplay(
    navController: NavHostController,
    categoryScreenState: CategoryScreenState,
    onEvent: (CategoryScreenEvents) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier,
        contentPadding = PaddingValues(4.dp)
    ) {
        items(categoryScreenState.categoryList.size) {catIndex ->
            CategoryItem(
                navController = navController,
                category = categoryScreenState.categoryList[catIndex],
                modifier = Modifier
                    .padding(
                        horizontal = dimensionResource(id = R.dimen.padding_x_small),
                        vertical = dimensionResource(id = R.dimen.padding_x_small)
                    )
            )

            if (catIndex >= categoryScreenState.categoryList.size - 1 && !categoryScreenState.isLoading) {
                onEvent(CategoryScreenEvents.OnPaginate())
            }
        }

    }
}


@Composable
fun CategoryItem(
    navController: NavHostController,
    category: Category,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .height(160.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = dimensionResource(id = R.dimen.padding_x_small),
                    vertical = dimensionResource(id = R.dimen.padding_small)
                )
        ) {
            CategoryImage(
                navController = navController,
                category = category,
                modifier = Modifier.weight(1f)
            )
            CategoryInformation(
                category = category,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
//            CategoryButton(navController=navController, category = category)
        }
    }
}

@Composable
fun CategoryImage(navController: NavHostController, category: Category, modifier: Modifier = Modifier) {
    val imageModel = ImageRequest.Builder(context = LocalContext.current)
        .data(EvvoliTmApi.BASE_URL + category.imageUrl)
        .crossfade(true)
        .build()

    val imageState = rememberAsyncImagePainter(model = imageModel).state

    Box(
        modifier = modifier
            .clickable {
                navController.navigate(Screen.CategoryProductsScreen.route + "/${category.id}")
            },
        contentAlignment = Alignment.Center
    ) {
        if (imageState is AsyncImagePainter.State.Error) {
            Icon(
                imageVector = Icons.Rounded.ImageNotSupported,
                contentDescription = getCategoryName(category)
            )
        } else {
            AsyncImage(
                model = imageModel,
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = getCategoryName(category),
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}

@Composable
fun CategoryInformation(category: Category, modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text(
            text = getCategoryName(category),
            style = MaterialTheme.typography.titleSmall,
        )
//        if (category.description != null) {
//            Text(
//                text = category.description ?: "",
//                style = MaterialTheme.typography.labelSmall,
//            )
//        }
    }
}

@Composable
fun CategoryButton(
    navController: NavHostController,
    category: Category,
    modifier: Modifier = Modifier
) {
    Button(onClick = {
//            Log.d("Nav", "ButtonOnClick => categorySlug = ${category.slug}")
//            Log.d("Nav",
//                "ButtonOnClick => Button = ${Screen.CategoryProductsScreen.route}/${category.id}"
//            )
//            Log.d("Nav", "ButtonOnClick => categoryId = ${category.id}")
            navController.navigate(Screen.CategoryProductsScreen.route + "/${category.id}")
        }
    ) {
        Text(text = "See Product")
    }
}


//@Composable
//fun ErrorScreen(navController: NavHostController, retryAction: () -> Unit, modifier: Modifier = Modifier) {
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



///**
// * The home screen displaying the loading message.
// */
//@Composable
//fun LoadingScreen(navController: NavHostController, modifier: Modifier = Modifier) {
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

