package com.example.evvolicatalogue.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.evvolicatalogue.R
import com.example.evvolicatalogue.utils.Screen


const val CATEGORIES = "Categories"
const val ADD_NEW_PRODUCT = "Add new product"
const val ABOUT_US = "About Us"

sealed class BottomNavItem(val screen: Screen, val icon: ImageVector, val label: String) {
    data object Categories : BottomNavItem(Screen.CategoriesScreen, Icons.Default.Home, CATEGORIES)
    data object About : BottomNavItem(Screen.AboutScreen, Icons.Default.Info, ABOUT_US)
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Categories,
        BottomNavItem.About
    )

    NavigationBar {
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        item.icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
                label = {
                    when (item.label) {
                        CATEGORIES -> Text(text = stringResource(id = R.string.categories))
                        ABOUT_US -> Text(text = stringResource(id = R.string.about_us))
                    }
                },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
private fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}


