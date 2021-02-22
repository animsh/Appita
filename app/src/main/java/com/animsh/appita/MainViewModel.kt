package com.animsh.appita

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.animsh.appita.data.Repository
import com.animsh.appita.models.FoodRecipe
import com.animsh.appita.util.NetworkResult
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * Created by animsh on 2/22/2021.
 */
class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    var foodRecipeResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        foodRecipeResponse.value = NetworkResult.Loading()
        if (hasNetworkConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                foodRecipeResponse.value = handleResponse(response)
            } catch (e: Exception) {
                foodRecipeResponse.value = NetworkResult.Error(message = "Recipes not found!!")
            }
        } else {
            foodRecipeResponse.value = NetworkResult.Error(message = "No Internet Connection!!")
        }
    }

    private fun handleResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error(message = "Timeout!!")
            }

            response.code() == 402 -> {
                return NetworkResult.Error(message = "Quota Exceeded!!")
            }

            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error(message = "Recipe not found!!")
            }

            response.isSuccessful -> {
                val foodRecipe = response.body()
                return NetworkResult.Success(foodRecipe!!)
            }

            else -> return NetworkResult.Error(message = response.message())
        }
    }

    fun hasNetworkConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}