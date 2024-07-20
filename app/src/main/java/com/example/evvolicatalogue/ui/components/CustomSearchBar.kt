package com.example.evvolicatalogue.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.evvolitm.R
import com.example.evvolitm.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(navController: NavHostController) {
    // Observe the current back stack entry
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    // Reset the query when the back stack entry changes
    LaunchedEffect(navBackStackEntry) {
        query = ""
    }

    SearchBar(
        modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end= 8.dp),
        query = query,
        onQueryChange = { query = it },
        onSearch = {
            active = false
            if (query.isEmpty()) query = " "
            navController.navigate(Screen.SearchProductsScreen.route + "/${query}") {
                // Ensure a new instance of the destination is created
                launchSingleTop = true
                restoreState = true
            }
        },
        active = active,
        onActiveChange = {
            active = it
        },
        placeholder =  {
            Text(stringResource(R.string.search))
        },
        leadingIcon =  {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.search_icon)
            )
        },
        trailingIcon = {
            if (active) {
                Icon(
                    modifier = Modifier
                        .clickable {
                          if (query.isNotEmpty()) {
                              query = ""
                          } else {
                              active = false
                          }
                        },
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close_icon)
                )
            }
        }
    ) {

    }
}
