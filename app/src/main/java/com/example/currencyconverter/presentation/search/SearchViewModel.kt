package com.example.currencyconverter.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.currencyconverter.KEY_IMAGE_URL
import com.example.currencyconverter.data.repository.PhotosRepository
import com.example.currencyconverter.workers.SavePhotoWorker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: PhotosRepository,
    private val workManager: WorkManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

//    init {
//        searchPhotos("city ")
//    }

    val photos = repository.database.photoDao.getAll()

    fun searchPhotos(query: String) = viewModelScope.launch(ioDispatcher) {
        repository.searchPhotos(query)
    }

    fun savePhoto(imageUrl: String) {
        val constraints = Constraints.Builder()
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val save = OneTimeWorkRequestBuilder<SavePhotoWorker>()
        save.setInputData(createInputDataForUrl(imageUrl))
        save.setConstraints(constraints)

        workManager.enqueue(save.build())
    }

    private fun createInputDataForUrl(imageUrl: String): Data {
        val builder = Data.Builder()
        builder.putString(KEY_IMAGE_URL, imageUrl)
        return builder.build()
    }
}