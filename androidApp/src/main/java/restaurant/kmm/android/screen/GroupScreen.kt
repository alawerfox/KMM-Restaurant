package restaurant.kmm.android.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import restaurant.kmm.GroupViewModel
import restaurant.kmm.android.R
import restaurant.kmm.model.Product
import restaurant.kmm.model.Restaurant

@Composable
fun GroupScreen(
    viewModel: GroupViewModel = koinViewModel(),
    restaurant: Restaurant,
    onProductClick: (List<Product>) -> Unit,
) {
    val groups by viewModel.groups.collectAsState()
    val error by viewModel.error.collectAsState(initial = "")

    var restaurantName by remember { mutableStateOf("") }
    var dialog by remember { mutableStateOf(false) }

    if (restaurantName.isBlank()) {
        restaurantName = restaurant.name
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (groups.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(100.dp),
                    color = Color.White,
                    strokeWidth = 10.dp
                )
            }
        } else {
            Column {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { dialog = true }
                ) {
                    Text(
                        text = restaurantName,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    Modifier.padding(16.dp),
                ) {
                    groups.forEach {
                        item {
                            Column(Modifier.clickable { onProductClick(it.products)}) {
                                AsyncImage(
                                    model = it.imgUrl,
                                    contentDescription = "",
                                    modifier = Modifier.height(120.dp)
                                )
                                Text(
                                    text = it.name,
                                    color = Color.White,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (dialog) {
        val restaurants by viewModel.restaurants.collectAsState()

        Dialog(onDismissRequest = { dialog = false }) {
            Box(
                Modifier.background(Color.White)
            ) {
                if (restaurants.isEmpty()) {
                    CircularProgressIndicator(
                        Modifier
                            .align(Alignment.Center)
                            .size(32.dp)
                    )
                } else {
                    Column {
                        restaurants.forEach {
                            Card(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.load(it.id)
                                        restaurantName = it.name
                                        dialog = false
                                    }
                            ) {
                                Text(modifier = Modifier.padding(16.dp), text = it.name)
                            }

                        }
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            viewModel.loadRestaurant()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.load(restaurant.id)
    }


    val context = LocalContext.current
    LaunchedEffect(error) {
        if (error.isNotBlank()) {
            Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show()
        }
    }
}