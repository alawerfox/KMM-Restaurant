package restaurant.kmm.model

@kotlinx.serialization.Serializable
data class Restaurant(
    val id: String,
    val name: String,
)