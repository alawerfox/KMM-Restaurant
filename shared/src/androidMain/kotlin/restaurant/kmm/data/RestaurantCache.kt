package restaurant.kmm.data

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import restaurant.kmm.model.Restaurant

actual class RestaurantCache(
    private val preferences: SharedPreferences
) {
    actual fun getRestaurantCode(): Restaurant? {
        val json = preferences.getString("restaurant_code", "") ?: ""
        if (json.isBlank()) return null
        return Json.decodeFromString(json)
    }

    actual fun setRestaurantCode(code: Restaurant) {
        preferences.edit {
            putString("restaurant_code", Json.encodeToString(code))
        }
    }
}