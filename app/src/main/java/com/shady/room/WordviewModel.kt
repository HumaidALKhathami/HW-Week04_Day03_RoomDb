package com.shady.room

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class WordViewModel(private val repository: WordRepository) : ViewModel() {

    val allWords : LiveData<List<Word>> = repository.allWords.asLiveData()

    fun insert (word: Word) = viewModelScope.launch {
        repository.insert(word)
    }
}

class WordviewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)){

            @Suppress("UNCHECKED_CAST")

           return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("unknown ViewModel class")
    }

}