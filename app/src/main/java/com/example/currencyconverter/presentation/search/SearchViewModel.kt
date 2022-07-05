package com.example.currencyconverter.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.currencyconverter.KEY_IMAGE_ID
import com.example.currencyconverter.KEY_IMAGE_URL
import com.example.currencyconverter.data.database.Photo
import com.example.currencyconverter.data.repository.PhotosRepository
import com.example.currencyconverter.workers.SavePhotoWorker
import com.example.currencyconverter.workers.SendAnalyticsToFirebaseWorker
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

    fun savePhoto(photo: Photo) {
        val constraints = Constraints.Builder()
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val save = OneTimeWorkRequestBuilder<SavePhotoWorker>()
            .setInputData(createInputDataForUrl(photo.urlFull))
            .setConstraints(constraints)
            .build()

        val send = OneTimeWorkRequestBuilder<SendAnalyticsToFirebaseWorker>()
            .setInputData(createInputDataForImageId(photo.id))
            .build()

//        val continuation = workManager.beginUniqueWork(
//            DOWNLOAD_IMAGE_WORK_NAME,
//            ExistingWorkPolicy.REPLACE,
//            send
//        )

        val continuation = workManager.beginWith(save).then(send).enqueue()
    }

    private fun createInputDataForImageId(photoId: Int): Data {
        val builder = Data.Builder()
        builder.putString(KEY_IMAGE_ID, photoId.toString())
        return builder.build()
    }

    private fun createInputDataForUrl(imageUrl: String): Data {
        val builder = Data.Builder()
        builder.putString(KEY_IMAGE_URL, imageUrl)
        return builder.build()
    }
}