package com.example.galactilist.ui.planetList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galactilist.data.PlanetRepository
import com.example.galactilist.network.responses.PlanetsResponse
import com.example.galactilist.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanetListViewModel @Inject constructor(private val planetRepository: PlanetRepository) : ViewModel() {
    private val _planetList: MutableLiveData<Resource<PlanetsResponse>> = MutableLiveData()
    val planetList: LiveData<Resource<PlanetsResponse>> = _planetList

    fun performGetPlanets(): LiveData<Resource<PlanetsResponse>> {
        _planetList.postValue(Resource.Loading)

        val handler = CoroutineExceptionHandler { _, exception ->
            _planetList.postValue(Resource.Error(exception))
        }

        viewModelScope.launch(handler) {
            planetRepository.getPlanetList().let { result ->
                when (result) {
                    is Resource.Success -> {
                        result.data?.let { data ->
                            _planetList.postValue(Resource.Success(data))
                        } ?: run {
                            _planetList.postValue(Resource.Error(Exception("Data is null")))
                        }
                    }
                    is Resource.FormattedError -> {
                        _planetList.postValue(Resource.FormattedError(result.data))
                    }
                    else -> {
                        _planetList.postValue(Resource.Error(Exception()))
                    }
                }
            }
        }
        return planetList
    }
}