package restaurant.kmm.domain

import restaurant.kmm.data.RestaurantRepository
import restaurant.kmm.model.Group

class GetGroupsUseCase(
private val restaurantRepository: RestaurantRepository
) {

    suspend operator fun invoke(code: String): Result<List<Group>> {
        return kotlin.runCatching {
            restaurantRepository.getGroups(code).sortedBy { it.sortWeight }
        }
    }
}