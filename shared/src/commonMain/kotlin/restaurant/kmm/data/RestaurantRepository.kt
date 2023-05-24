package restaurant.kmm.data

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.*
import restaurant.kmm.model.Group
import restaurant.kmm.model.Product
import restaurant.kmm.model.Restaurant

class RestaurantRepository(private val httpClient: HttpClient) {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    suspend fun getRestaurant(): List<Restaurant> {
        val body = httpClient.request<String> {
            url("http://deluxe.mnogo.menu/api/json/restaurants")
        }

        val map = json.decodeFromString<Map<String, JsonElement>>(body)

        val error = map["error"]
        if (error is JsonObject) {
            val code = error["code"] as JsonPrimitive
            if (code.int == 1) throw Exception((error["message"] as JsonPrimitive).content)
        }

        val answer = map["answer"]
        if (answer is JsonObject) {
            return answer.entries.mapNotNull { (_, value) ->
                if (value is JsonObject) {
                    val active = value["Active"] as JsonPrimitive
                    if (active.boolean) {
                        return@mapNotNull Restaurant(
                            (value["ID"] as JsonPrimitive).content,
                            (value["Name"] as JsonPrimitive).content
                        )
                    }
                }

                return@mapNotNull null
            }
        }

        return emptyList()
    }

    suspend fun getGroups(restaurantCode: String): List<Group> {
        val body = httpClient.request<String> {
            url("http://deluxe.mnogo.menu/api/json/lightnomenclature?ssid=918e5647-483a-4ead-95e2-1ff65c1421b7&restaurant=$restaurantCode")
        }

        val map = json.decodeFromString<Map<String, JsonElement>>(body)

        val error = map["error"]
        if (error is JsonObject) {
            val code = error["code"] as JsonPrimitive
            if (code.int == 1) throw Exception((error["message"] as JsonPrimitive).content)
        }

        val answer = map["answer"]
        if (answer is JsonObject) {
            val rootGroupId = answer["RootGroupID"] as JsonPrimitive

            val groupMap = answer["GroupsMap"] as JsonObject
            val productsMap = answer["ProductsMap"] as JsonObject

            return groupMap.entries.mapNotNull { (_, value) ->
                if (value is JsonObject) {
                    val active = value["Active"] as JsonPrimitive
                    val parentGroupID = value["ParentGroupID"] as JsonPrimitive
                    val childProductsID = value["ChildProductsID"] as JsonArray

                    if (active.boolean && parentGroupID.content == rootGroupId.content && childProductsID.isNotEmpty()) {
                        val imgUrl = value["Img"].let {
                            if (it is JsonObject) {
                                val domain = it["Domain"] as JsonPrimitive
                                val path = it["Path"] as JsonPrimitive

                                if (domain.content.isBlank()) {
                                    "http://deluxe.mnogo.menu${path.content}"
                                } else {
                                    domain.content + path.content
                                }
                            } else {
                                ""
                            }
                        }

                        val products = childProductsID.mapNotNull {
                            val productId = (it as JsonPrimitive).content
                            val product = productsMap[productId]

                            if (product is JsonObject) {

                                val normalImgUrl = product["NormalImageURL"].let {
                                    if (it is JsonObject) {
                                        val domain = it["Domain"] as JsonPrimitive
                                        val path = it["Path"] as JsonPrimitive

                                        if (domain.content.isBlank()) {
                                            "http://deluxe.mnogo.menu${path.content}"
                                        } else {
                                            domain.content + path.content
                                        }
                                    } else {
                                        ""
                                    }
                                }


                                return@mapNotNull Product(
                                    (product["ID"] as JsonPrimitive).content,
                                    (product["Name"] as JsonPrimitive).content,
                                    normalImgUrl
                                )
                            }

                            return@mapNotNull null
                        }

                        return@mapNotNull Group(
                            (value["ID"] as JsonPrimitive).content,
                            (value["Name"] as JsonPrimitive).content,
                            imgUrl,
                            (value["SortWeight"] as JsonPrimitive).int,
                            products
                        )
                    }
                }

                return@mapNotNull null
            }
        }

        return emptyList()
    }
}