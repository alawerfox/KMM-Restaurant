package restaurant.kmm.domain

import restaurant.kmm.data.RestaurantRepository
import restaurant.kmm.model.Restaurant

class GetRestaurantsUseCase(
    private val restaurantRepository: RestaurantRepository
) {

    suspend operator fun invoke(): Result<List<Restaurant>> {
        return kotlin.runCatching {
            restaurantRepository.getRestaurant()
        }
    }
}