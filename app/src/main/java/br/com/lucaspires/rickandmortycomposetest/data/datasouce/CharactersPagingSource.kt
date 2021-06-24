package br.com.lucaspires.rickandmortycomposetest.data.datasouce

import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.lucaspires.rickandmortycomposetest.data.model.SimpleCharacterModel
import br.com.lucaspires.rickandmortycomposetest.data.service.RickAndMortyService

internal class CharactersPagingSource(private val rickAndMortyService: RickAndMortyService) :
    PagingSource<Int, SimpleCharacterModel>() {

    private val initialPage = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SimpleCharacterModel> {
        try {
            val next = params.key ?: initialPage
            val response = rickAndMortyService.getAllCharacters(next)

            val result = response.results?.map { it.toSimpleCharacterModel() } ?: emptyList()

            val hasNextPage = response.info?.next != null

            return LoadResult.Page(
                data = result,
                prevKey = if (next == 1) null else next - 1,
                nextKey = if (hasNextPage) next + 1 else null
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SimpleCharacterModel>): Int? {
        return state.anchorPosition
    }

}