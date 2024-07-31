package com.example.evvolicatalogue.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.evvolicatalogue.R
import com.example.evvolicatalogue.data.local.entities.CategoryEntity
import com.example.evvolicatalogue.utils.Screen
import com.example.evvolicatalogue.viewmodel.CategoryViewModel
import kotlinx.coroutines.flow.StateFlow


@Composable
fun CategoriesUpdateDeleteScreen(
    navController: NavHostController,
    categoryViewModel: CategoryViewModel,
    categories: StateFlow<PagingData<CategoryEntity>>,
    modifier: Modifier = Modifier
) {
    CategoryListForUpdateDelete(
        navController = navController,
        categoryViewModel = categoryViewModel,
        categories = categories,
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
fun CategoryListForUpdateDelete(
    navController: NavHostController,
    categoryViewModel: CategoryViewModel,
    categories: StateFlow<PagingData<CategoryEntity>>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val lazyPagingItems = categories.collectAsLazyPagingItems()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier,
        contentPadding = PaddingValues(4.dp)
    ) {
        items(lazyPagingItems.itemCount) { index ->
            val category = lazyPagingItems[index]
            if (category != null ){
                CategoryItemForUpdateDelete(
                    navController = navController,
                    categoryViewModel = categoryViewModel,
                    category = category,
                    modifier = Modifier
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.padding_x_small),
                            vertical = dimensionResource(id = R.dimen.padding_x_small)
                        )
                )
            }
        }
    }
}


@Composable
fun CategoryItemForUpdateDelete(
    navController: NavHostController,
    categoryViewModel: CategoryViewModel,
    category: CategoryEntity,
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
            CategoryImageForUpdateDelete(
                navController = navController,
                category = category,
                modifier = Modifier.weight(1f)
            )
            CategoryInformation(
                category = category,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
            CategoryUpdateDeleteButtons(
                navController = navController,
                categoryViewModel = categoryViewModel,
                category = category
            )
        }
    }
}

@Composable
fun CategoryImageForUpdateDelete(
    navController: NavHostController,
    category: CategoryEntity,
    modifier: Modifier = Modifier
) {
    val imageModel = ImageRequest.Builder(context = LocalContext.current)
        .data(category.imageUrl)
        .crossfade(true)
        .build()

    val imageState = rememberAsyncImagePainter(model = imageModel).state

    Box(
        modifier = modifier
            .clickable {
                navController.navigate(Screen.CategoryUpdateDeleteScreen.route + "/${category.id}")
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
fun CategoryUpdateDeleteButtons(
    navController: NavHostController,
    categoryViewModel: CategoryViewModel,
    category: CategoryEntity,
    modifier: Modifier = Modifier
) {
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta),
        onClick = {
            navController.navigate(
                Screen.CategoryUpdateDeleteScreen.route + "/${category.id}"
            )
        }
    ) {
        Text(
            text = "Update",
            fontSize = 12.sp
        )
    }
}

