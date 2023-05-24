package restaurant.kmm.android.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import restaurant.kmm.RestaurantViewModel
import restaurant.kmm.android.R
import restaurant.kmm.model.Restaurant

@Composable
fun RestaurantScreen(
    viewModel: RestaurantViewModel = koinViewModel(),
    onRestaurantClick: (Restaurant) -> Unit
) {
    val restaurants by viewModel.restaurants.collectAsState()
    val code by viewModel.restaurantCode.collectAsState(initial = null)
    val error by viewModel.error.collectAsState(initial = "")

    Box(Modifier.fillMaxSize()) {
        if (restaurants.isEmpty()) {
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
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                restaurants.forEach {
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.save(it)
                                onRestaurantClick(it)
                            }
                    ) {
                        Text(modifier = Modifier.padding(16.dp), text = it.name)
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.load()
    }

    LaunchedEffect(code) {
        if (code != null) {
            onRestaurantClick(code!!)
        }
    }

    val context = LocalContext.current
    LaunchedEffect(error) {
        if (error.isNotBlank()) {
            Toast.makeText(context, R.string.error, Toast.LENGTH_SHORT).show()
        }
    }
}