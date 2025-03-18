package com.example.galactilist.network.api

import com.example.galactilist.network.responses.PlanetsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PlanetAPI {
  @GET("planets/")
  suspend fun getPlanets(@Query("page") page: Int): Response<PlanetsResponse>
}