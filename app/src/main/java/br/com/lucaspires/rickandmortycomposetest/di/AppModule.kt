package br.com.lucaspires.rickandmortycomposetest.di

import br.com.lucaspires.rickandmortycomposetest.data.service.RickAndMortyService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient.Builder().apply {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }.build())
        .build()


    @Singleton
    @Provides
    fun providesRickMortyService(retrofit: Retrofit): RickAndMortyService =
        retrofit.create(RickAndMortyService::class.java)
}
