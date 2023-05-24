package restaurant.kmm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import restaurant.kmm.domain.GetGroupsUseCase
import restaurant.kmm.domain.GetRestaurantsUseCase
import restaurant.kmm.model.Group
import restaurant.kmm.model.Restaurant

class GroupViewModel(
    private val getGroupsUseCase: GetGroupsUseCase,
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
) : ViewModel() {

    val groups = MutableStateFlow<List<Group>>(emptyList())
    val restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val error = MutableSharedFlow<String>()

    fun load(code: String) {
        groups.value = emptyList()
        viewModelScope.launch {
            val result = getGroupsUseCase(code)
            if (result.isFailure) {
                error.emit(result.exceptionOrNull()?.localizedMessage ?: "")
                return@launch
            }

            groups.value = result.getOrElse { emptyList() }
        }
    }

    fun loadRestaurant() {
        viewModelScope.launch {
            val result = getRestaurantsUseCase()
            if (result.isFailure) {
                error.emit(result.exceptionOrNull()?.localizedMessage ?: "")
                return@launch
            }

            restaurants.value =result.getOrElse { emptyList() }
        }
    }
}