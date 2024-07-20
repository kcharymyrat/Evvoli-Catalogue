package com.example.evvolicatalogue.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evvolicatalogue.R

@Composable
fun AboutScreen() {
    println(AppCompatDelegate.getApplicationLocales())
    println("AppCompatDelegate.getApplicationLocales()[0]?.language " +
            "= ${AppCompatDelegate.getApplicationLocales()[0]?.language}")
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            CompanyLogo()
            Spacer(modifier = Modifier.height(16.dp))
            CompanyDescription()
            Spacer(modifier = Modifier.height(16.dp))
            ContactInformation()
        }
    }
}

@Composable
fun CompanyLogo() {
    val logoImage = if (isSystemInDarkTheme()) {
        painterResource(id = R.drawable.evvoli_logo_w1) // Image for dark theme
    } else {
        painterResource(R.drawable.evvoli_logo_b1) // Image for light theme
    }

    Image(
        painter = logoImage,
        contentDescription = "Company Logo",
        modifier = Modifier.size(100.dp)
    )
}

@Composable
fun CompanyDescription() {
    Text(
        text = stringResource(R.string.evvoli_company_description),
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
fun ContactInformation() {
    Column {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                val latitude: Double = 37.94038485947134
                val longitude: Double = 58.42375763825806
                val placeId = "ChIJJ5liW8r9bz8RSsMBKfj7uYw"
                LocationDetail(
                    icon = Icons.Default.Place,
                    detail = "Hoja Ahmet Yasawy St, Ashgabat 744000, Turkmenistan",
                    latitude = latitude,
                    longitude = longitude,
                    placeId = placeId
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {},
                    ) {

                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Gundogar Bazar D3, Ashgabat 744000, Turkmenistan",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                PhoneDetail(Icons.Default.Phone, "+993 65 726468")
                EmailDetail(Icons.Default.Email, "contact@evvoli.com")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                val latitude: Double = 37.59980287678001
                val longitude: Double = 61.87317372474693
                val placeId = "ChIJMckIgryPQT8Rv44UlQUOgNA"
                LocationDetail(
                    icon = Icons.Default.Place,
                    detail = "HVXF+V7F, Mary, Turkmenistan",
                    latitude = latitude,
                    longitude = longitude,
                    placeId = placeId,
                )
                PhoneDetail(Icons.Default.Phone, "+993 61 004770")
                EmailDetail(Icons.Default.Email, "contact@evvoli.com")
            }
        }

    }
}

@Composable
fun LocationDetail(
    icon: ImageVector,
    detail: String,
    latitude: Double,
    longitude: Double,
    placeId: String
) {
    val context = LocalContext.current
    val gmmIntentUri = Uri.parse(
        "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude&query_place_id=$placeId"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)
            },
        ) {
            Icon(
                imageVector = icon,
                contentDescription = detail,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            detail,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable {
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)
            }
        )
    }
}


@Composable
fun PhoneDetail(icon: ImageVector, detail: String) {
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$detail")
                }
                context.startActivity(intent)
            },
        ) {
            Icon(
                imageVector = icon,
                contentDescription = detail,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            detail,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$detail")
                }
                context.startActivity(intent)
            }
        )
    }
}


@Composable
fun EmailDetail(icon: ImageVector, detail: String) {
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$detail")
                }
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            },
        ) {
            Icon(
                imageVector = icon,
                contentDescription = detail,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            detail,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:$detail")
                }
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }
        )
    }
}


@Composable
fun LocationLatLongDetail(icon: ImageVector, detail: String, latitude: Double, longitude: Double) {
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                val gmmIntentUri = Uri.parse(
                    "https://www.google.com/maps/search/?api=1&query=$latitude,$longitude"
                )
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                context.startActivity(mapIntent)
            },
        ) {
            Icon(
                imageVector = icon,
                contentDescription = detail,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(detail, style = MaterialTheme.typography.bodyMedium)
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AboutApp() {
    AboutScreen()
}