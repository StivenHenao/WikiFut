package com.wikifut.app.di

import com.wikifut.app.api.PartidosApi
import com.wikifut.app.api.PlayerApi
import com.wikifut.app.api.SeasonApi
import com.wikifut.app.api.LigaDetalleApi
import com.wikifut.app.api.LigaApi
import com.wikifut.app.utils.Constans
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import okhttp3.Response

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): Interceptor {
        return ApiKeyInterceptor(Constans.X_RAPIDAPI_KEY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constans.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): PartidosApi {
        return retrofit.create(PartidosApi::class.java)
    }

    @Provides
    @Singleton
    fun providePlayerApi(retrofit: Retrofit): PlayerApi {
        return retrofit.create(PlayerApi::class.java)
    }

    @Provides
    fun provideSeasonApi(retrofit: Retrofit): SeasonApi =
        retrofit.create(SeasonApi::class.java)

    @Provides
    fun provideLigaDetalleApi(retrofit: Retrofit): LigaDetalleApi =
        retrofit.create(LigaDetalleApi::class.java)

    @Provides
    fun provideLigaApi(retrofit: Retrofit): LigaApi =
        retrofit.create(LigaApi::class.java)

    class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val newRequest = originalRequest.newBuilder()
                .header("x-rapidapi-key", apiKey)
                .build()
            return chain.proceed(newRequest)
        }
    }


}