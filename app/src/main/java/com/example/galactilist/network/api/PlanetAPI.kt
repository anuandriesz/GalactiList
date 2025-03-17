package com.example.galactilist.network.api

import com.example.galactilist.network.responses.PlanetsResponse
import retrofit2.Response
import retrofit2.http.GET

interface PlanetAPI {
  @GET("planets")
  suspend fun getPlanets(): Response<PlanetsResponse>
}