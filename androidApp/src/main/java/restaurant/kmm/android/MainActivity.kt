package restaurant.kmm.android

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import restaurant.kmm.android.screen.GroupScreen
import restaurant.kmm.android.screen.ProductScreen
import restaurant.kmm.android.screen.RestaurantScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                NavigationGraph(navController = navController)
            }
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    val restaurantScreen = "restaurant"
    val groupScreen = "group"
    val productScreen = "product"

    NavHost(
        navController = navController,
        startDestination = restaurantScreen
    ) {

        composable(restaurantScreen) {
            RestaurantScreen {
                val json = Uri.encode(Json.encodeToString(it))
                navController.navigate("$groupScreen/$json")
            }
        }

        composable(
            route = "$groupScreen/{arg}",
            arguments = listOf(navArgument("arg") { type = NavType.StringType })
        ) {
            val arg = Uri.decode(it.arguments?.getString("arg") ?: "")

            GroupScreen(
                restaurant = Json.decodeFromString(arg),
                onProductClick = {
                    val json = Uri.encode(Json.encodeToString(it))
                    navController.navigate("$productScreen/$json")
                }
            )
        }

        composable(
            route = "$productScreen/{arg}",
            arguments = listOf(navArgument("arg") { type = NavType.StringType })
        ) {
            val arg = Uri.decode(it.arguments?.getString("arg") ?: "")
            ProductScreen(Json.decodeFromString(arg))
        }
    }
}