package com.example.galactilist.data

import com.example.galactilist.network.responses.PlanetsResponse
import com.example.galactilist.utils.Resource

interface PlanetInterface {
    suspend fun getPlanetList(page: Int): Resource<PlanetsResponse?>
}