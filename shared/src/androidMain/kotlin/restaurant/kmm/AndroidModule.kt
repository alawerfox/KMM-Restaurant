package restaurant.kmm

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import restaurant.kmm.data.RestaurantCache

val androidModule = module {
    single {
        HttpClient(OkHttp) {
            install(JsonFeature) {
               serializer = KotlinxSerializer(
                   Json {
                       isLenient = true
                       ignoreUnknownKeys = true
                   }
               )
            }
        }
    }

    single {
        val preferences = androidContext().getSharedPreferences("restaurant_cache", 0)

        RestaurantCache(preferences)
    }

    viewModel { RestaurantViewModel(get(), get()) }
    viewModel { GroupViewModel(get(), get()) }
}