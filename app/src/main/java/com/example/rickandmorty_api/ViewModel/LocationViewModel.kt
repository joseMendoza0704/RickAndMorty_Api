package com.example.rickandmorty_api.ViewModel




import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty_api.models.Location
import com.example.rickandmorty_api.models.LocationList
import com.example.rickandmorty_api.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationViewModel : ViewModel() {


    private val _locations = MutableStateFlow<List<Location>>(emptyList())
    val locations: StateFlow<List<Location>> get() = _locations

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage


    fun getLocations() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            ApiClient.api.getLocations().enqueue(object : Callback<LocationList> {
                override fun onResponse(call: Call<LocationList>, response: Response<LocationList>) {
                    if (response.isSuccessful) {
                        _locations.value = response.body()?.results ?: emptyList()
                    } else {
                        _errorMessage.value = "Error: ${response.code()}"
                    }
                    _isLoading.value = false
                }

                override fun onFailure(call: Call<LocationList>, t: Throwable) {
                    _errorMessage.value = t.message
                    _isLoading.value = false
                    Log.e("LocationViewModel", "Error: ${t.message}")
                }
            })
        }
    }
}