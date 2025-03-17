package com.example.galactilist.di

import com.example.galactilist.data.PlanetRepository
import com.example.galactilist.network.api.PlanetAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideIoDispatcher() = Dispatchers.IO

    @Provides
    @Singleton
    fun providePlanetRepository (planetAPI: PlanetAPI) : PlanetRepository {
        return PlanetRepository(planetAPI)
    }
}