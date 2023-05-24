package restaurant.kmm

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import restaurant.kmm.data.RestaurantRepository
import restaurant.kmm.domain.GetGroupsUseCase
import restaurant.kmm.domain.GetRestaurantsUseCase

val platformModule = module {
    singleOf(::RestaurantRepository)

    single { GetRestaurantsUseCase(get()) }
    single { GetGroupsUseCase(get()) }
}
