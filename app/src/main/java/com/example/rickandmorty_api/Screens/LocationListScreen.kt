package com.example.rickandmorty_api.Screens




import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rickandmorty_api.network.ApiClient
import com.example.rickandmorty_api.models.Location
import com.example.rickandmorty_api.models.LocationList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LocationListScreen() {
    var locations by remember { mutableStateOf<List<Location>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    LaunchedEffect(true) {
        ApiClient.api.getLocations().enqueue(object : Callback<LocationList> {
            override fun onResponse(call: Call<LocationList>, response: Response<LocationList>) {
                if (response.isSuccessful) {
                    locations = response.body()?.results ?: emptyList()
                } else {
                    errorMessage = "Error: ${response.code()}"
                }
                isLoading = false
            }

            override fun onFailure(call: Call<LocationList>, t: Throwable) {
                errorMessage = t.message
                isLoading = false
            }
        })
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.primary)
    } else if (errorMessage != null) {
        Text(text = "Error: $errorMessage", color = Color.Red, style = MaterialTheme.typography.bodyLarge)
    } else {
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1e2838)),
            contentPadding = PaddingValues(16.dp)) {
            items(locations) { location ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF38761D)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(location.name, style = MaterialTheme.typography.bodyLarge, color = Color.White)
                        Text(location.type, style = MaterialTheme.typography.bodySmall, color = Color.White)
                        Text(location.dimension, style = MaterialTheme.typography.bodySmall, color = Color.White)
                    }
                }
            }
        }
    }
}