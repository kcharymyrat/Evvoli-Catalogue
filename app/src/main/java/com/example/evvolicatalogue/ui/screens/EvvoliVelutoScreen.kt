package com.example.evvolicatalogue.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.evvolitm.R
import com.example.evvolitm.util.Screen

@Composable
fun EvvoliVelutoScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        EvvoliChoiceScreen(navController = navController, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(16.dp))
        VelutoChoiceScreen(navController = navController, modifier = Modifier.weight(1f))
    }
}

@Composable
fun EvvoliChoiceScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = modifier
            .clickable(onClick = { navController.navigate(Screen.CategoriesScreen.route) }),
    ) {
        Box(
            contentAlignment = Alignment.Center, // This aligns the child composable to the center
            modifier = Modifier.fillMaxSize() // Ensures the Box fills the Card
        ) {
            Image(
                painter = painterResource(id = R.drawable.evvoli_logo_w1),
                contentDescription = "Evvoli Logo",
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black),
                contentScale = ContentScale.Crop
            )
        }

    }
}

@Composable
fun VelutoChoiceScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable(onClick = { }),
    ) {
        Image(
            painter = painterResource(id = R.drawable.veluto_logo),
            contentDescription = "Veluto Logo",
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black),
            contentScale = ContentScale.Crop
        )
    }
}