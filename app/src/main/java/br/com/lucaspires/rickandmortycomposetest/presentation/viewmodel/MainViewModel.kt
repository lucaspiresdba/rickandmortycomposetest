package br.com.lucaspires.rickandmortycomposetest.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import br.com.lucaspires.rickandmortycomposetest.data.datasouce.CharacterRepository
import br.com.lucaspires.rickandmortycomposetest.data.model.SimpleCharacterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    val loadingPerson = MutableLiveData(false)

    val personList = MutableLiveData<List<SimpleCharacterModel>>()

    val noContent = MutableLiveData(false)

    fun getCharacters(): Flow<PagingData<SimpleCharacterModel>> {
        return characterRepository.getCharacters()
            .cachedIn(viewModelScope)
    }

    fun getCharacterByName(name: String) {
        viewModelScope.launch {
            try {
                noContent.value = false
                loadingPerson.value = true
                characterRepository.getCharacterByName(name)?.let { personList.value = it }
            } catch (e: Exception) {
                when (e) {
                    is HttpException -> {
                        if (e.code() == 404) {
                            noContent.value = true
                            personList.value = emptyList()
                        }
                    }
                }
            } finally {
                loadingPerson.value = false
            }
        }
    }

}