package restaurant.kmm.model

@kotlinx.serialization.Serializable
data class Product(
    val id: String,
    val name: String,
    val imgUrl: String
)
