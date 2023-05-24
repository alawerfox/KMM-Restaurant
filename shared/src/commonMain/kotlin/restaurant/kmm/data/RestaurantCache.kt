package restaurant.kmm.data

import restaurant.kmm.model.Restaurant

expect class RestaurantCache {

    fun getRestaurantCode(): Restaurant?

    fun setRestaurantCode(code: Restaurant)

}