package restaurant.kmm.model

data class Group(
    val id: String,
    val name: String,
    val imgUrl: String,
    val sortWeight: Int,
    val products: List<Product>
)
