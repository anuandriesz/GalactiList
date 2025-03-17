package com.example.galactilist.di

import android.app.Application
import android.util.Log
import com.example.galactilist.constants.Constants
import com.example.galactilist.network.api.PlanetAPI
import com.example.galactilist.utils.ConnectivityChecker
import com.google.gson.GsonBuilder
import com.moczul.ok2curl.CurlInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class NetworkModule {
        @Provides
        @Singleton
        open fun provideOkHttpClient(context: Application): OkHttpClient {

            return OkHttpClient.Builder()
                .readTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(NoConnectivityInterceptor())
                .addInterceptor(AuthorizationHeaderInterceptor())
                .addInterceptor(CurlInterceptor { message ->
                    Log.d(
                        "okhttp.OkHttpClient",
                        "log: $message "
                    )
                })
                .build()
        }

        @Provides
        @Singleton
        open fun provideStarWarsApi(client: OkHttpClient): PlanetAPI {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            return  Retrofit.Builder()
                .baseUrl(Constants.API_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
                .create(PlanetAPI::class.java)
        }
    }

    class NoConnectivityInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            if (!ConnectivityChecker.instance.isOnline()) {
                throw IOException("No internet connection")
            } else {
                return chain.proceed(chain.request())
            }
        }
    }

    class AuthorizationHeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
                request = request.newBuilder()
                    .build()

            return chain.proceed(request)
        }
    }

