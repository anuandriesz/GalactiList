package com.example.galactilist.data

import com.example.galactilist.network.responses.PlanetsResponse
import com.example.galactilist.network.api.PlanetAPI
import com.example.galactilist.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlanetRepository(private val planetAPI: PlanetAPI) : PlanetInterface {
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    override suspend fun getPlanetList(page: Int): Resource<PlanetsResponse?> {
        return withContext(ioDispatcher) {
            try {
                val response = planetAPI.getPlanets(page)
                if (response.isSuccessful) {
                    Resource.Success(response.body())
                } else {
                    Resource.Error(response.message())
                }
            } catch (e: Exception) {
                Resource.Error(e.message ?: "An error occurred")
            }
        }
    }
}