package restaurant.kmm.android.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import restaurant.kmm.model.Product

@Composable
fun ProductScreen(products: List<Product>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        Modifier.padding(16.dp)
    ) {
        products.forEach {
            item {
                Column (
                    Modifier.padding(8.dp)
                ){
                    AsyncImage(model = it.imgUrl, contentDescription = "", modifier = Modifier.height(120.dp))
                    Text(text = it.name, color = Color.White)
                }
            }
        }
    }
}