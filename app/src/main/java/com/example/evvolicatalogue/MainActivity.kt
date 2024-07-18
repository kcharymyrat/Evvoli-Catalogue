package com.example.evvolicatalogue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.evvolicatalogue.ui.theme.EvvoliCatalogueTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EvvoliCatalogueTheme {

                val mainViewModel = hiltViewModel<MainViewModel>()
                val navController: NavHostController = rememberNavController()

                val startDestination =

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GreetingText(
                        message = "Android",
                        from = "Chary",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Composable
fun GreetingText(message: String, from: String, modifier: Modifier = Modifier) {
    Text(
        text = from,
        fontSize = 36.sp
    )
    Text(
        text = message,
        fontSize = 100.sp,
        lineHeight = 116.sp,
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Default preview"
)
@Composable
fun GreetingPreview() {
    EvvoliCatalogueTheme {
        GreetingText("Happy Birthday!", "From Chary")
    }
}