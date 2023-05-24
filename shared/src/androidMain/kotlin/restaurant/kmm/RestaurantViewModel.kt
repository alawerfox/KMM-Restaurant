package restaurant.kmm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import restaurant.kmm.data.RestaurantCache
import restaurant.kmm.domain.GetRestaurantsUseCase
import restaurant.kmm.model.Restaurant

class RestaurantViewModel(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val restaurantCache: RestaurantCache
) : ViewModel() {

    val restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurantCode = MutableSharedFlow<Restaurant>()
    val error = MutableSharedFlow<String>()

    fun load() {
        viewModelScope.launch {
            val restaurant = restaurantCache.getRestaurantCode()
            if (restaurant != null) {
                restaurantCode.emit(restaurant)
                return@launch
            }

            val result = getRestaurantsUseCase()
            if (result.isFailure) {
                error.emit(result.exceptionOrNull()?.localizedMessage ?: "")
                return@launch
            }

            restaurants.value = result.getOrElse { emptyList() }
        }
    }

    fun save(restaurant: Restaurant) {
        restaurantCache.setRestaurantCode(restaurant)
    }
}