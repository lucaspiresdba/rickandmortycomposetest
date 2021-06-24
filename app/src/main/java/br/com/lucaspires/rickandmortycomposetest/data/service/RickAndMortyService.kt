package br.com.lucaspires.rickandmortycomposetest.data.service

import br.com.lucaspires.rickandmortycomposetest.data.model.CharactersListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RickAndMortyService {
    @GET("character")
    suspend fun getAllCharacters(@Query("page") page: Int): CharactersListResponse

    @GET("character")
    suspend fun getCharacterByName(@Query("name") name: String): CharactersListResponse
}