package com.example.kode_testapp.screens.details_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kode_testapp.repositories.WorkersRepository
import com.example.kode_testapp.retrofit.Worker

class DetailsPageViewModel(private val workersRepository: WorkersRepository) : ViewModel() {

    private val uiState = MutableLiveData(WorkerUiState(null, null))
    val uiStateChanges: LiveData<WorkerUiState> = uiState

    private val currentUiState: WorkerUiState
        get() = requireNotNull(uiState.value)

    fun loadWorker(id: String) {
        if (id == "EMPTY") {
            val newUiState = currentUiState.copy(
                worker = null,
                error = Throwable()
            )
            uiState.value = newUiState
        }

        val workerList = workersRepository.workersInMemoryCache.toList()
        val position = workersRepository.workersInMemoryCache.indexOfFirst { worker ->
            worker.id == id
        }

        if (position == -1) {
            val newUiState = currentUiState.copy(
                worker = null,
                error = Throwable()
            )
            uiState.value = newUiState
        } else {
            val newUiState = currentUiState.copy(
                worker = workerList[position],
                error = null
            )
            uiState.value = newUiState
        }

    }

}

data class WorkerUiState(
    val worker: Worker?,
    val error: Throwable?
)