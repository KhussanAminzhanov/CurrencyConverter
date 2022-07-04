package com.example.currencyconverter.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyconverter.data.repository.PhotosRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: PhotosRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    val photos = repository.database.photoDao.getAll()

    fun searchPhotos(query: String) = viewModelScope.launch(ioDispatcher) {
        repository.searchPhotos(query)
    }
}