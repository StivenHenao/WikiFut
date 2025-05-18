package com.wikifut.app.repository

//package com.wikifut.app.di

//import com.wikifut.app.repository.FavoritesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFavoritesRepository(): FavoritesRepository {
        return FavoritesRepository()
    }
}
