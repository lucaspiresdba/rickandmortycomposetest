package br.com.lucaspires.rickandmortycomposetest.data.datasouce

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import br.com.lucaspires.rickandmortycomposetest.data.model.SimpleCharacterModel
import br.com.lucaspires.rickandmortycomposetest.data.service.RickAndMortyService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CharacterRepository @Inject constructor(private val rickAndMortyService: RickAndMortyService) {

    fun getCharacters(): Flow<PagingData<SimpleCharacterModel>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { CharactersPagingSource(rickAndMortyService) }
        ).flow
    }

    suspend fun getCharacterByName(name: String): List<SimpleCharacterModel>? =
        rickAndMortyService.getCharacterByName(name)
            .results
            ?.map { it.toSimpleCharacterModel() }
}
