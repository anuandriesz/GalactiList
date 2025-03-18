package com.example.galactilist.ui.planetList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galactilist.data.PlanetRepository
import com.example.galactilist.network.responses.Planet
import com.example.galactilist.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanetListViewModel @Inject constructor(private val planetRepository: PlanetRepository) : ViewModel() {
    // LiveData holding the current state of the planet list.
    private val _planetList = MutableLiveData<Resource<List<Planet>>>()
    val planetList: LiveData<Resource<List<Planet>>> = _planetList

    // List to store all fetched planets.
    private val allPlanets = mutableListOf<Planet>()
    // Current page number for pagination.
    private var currentPage = 1
    // Flag to indicate if data is currently being loaded.
    private var isLoading = false
    // Flag to indicate if the last page has been reached.
    private var isLastPage = false
    // Store last scroll position.
    private var lastVisiblePosition = 0

    /**
     * Fetches the list of planets from the repository.
     * Updates the LiveData with the current state of the data.
     */
    fun performGetPlanets() {
        if (isLoading || isLastPage) return // Prevent duplicate calls

        _planetList.postValue(Resource.Loading)
        isLoading = true

        val handler = CoroutineExceptionHandler { _, exception ->
            _planetList.postValue(Resource.Error(exception))
            isLoading = false
        }

        viewModelScope.launch(handler) {
            when (val result = planetRepository.getPlanetList(currentPage)) {
                is Resource.Success -> {
                    result.data?.let { response ->
                        allPlanets.addAll(response.results)
                        _planetList.postValue(Resource.Success(ArrayList(allPlanets)))

                        if (response.next == null) {
                            isLastPage = true
                        } else {
                            currentPage++
                        }
                    } ?: run {
                        _planetList.postValue(Resource.Error(Exception("Data is null")))
                    }
                }
                is Resource.Error -> {
                    _planetList.postValue(Resource.Error(result.exception))
                }
                else -> {
                    _planetList.postValue(Resource.Error(Exception("Unexpected error occurred")))
                }
            }
            isLoading = false
        }
    }
}