package com.nilax.weatherapp.di

import com.nilax.weatherapp.common.NetworkConstants
import com.nilax.weatherapp.data.remote.WeatherApi
import com.nilax.weatherapp.data.repo.WeatherRepositoryImpl
import com.nilax.weatherapp.domain.repo.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun getRetrofit(): WeatherApi {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val httpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(NetworkConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(WeatherApi::class.java)

    }

    @Singleton
    @Provides
    fun getRepository(api: WeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(api)
    }

    @Provides
    fun provideCurrentDate(): Date {
        return Date()
    }

}