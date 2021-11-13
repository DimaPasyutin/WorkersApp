package com.example.kode_testapp.screens.main_page

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kode_testapp.App
import com.example.kode_testapp.pojo.DepartmentType
import com.example.kode_testapp.retrofit.Worker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MainPageViewModel(val app: App) : ViewModel() {

    private val uiState = MutableLiveData(WorkersUiState(emptyList(), null, false))
    val uiStateChanges: LiveData<WorkersUiState> = uiState

    private val currentUiState: WorkersUiState
        get() = requireNotNull(uiState.value)

    private val asyncWorkers = CompositeDisposable()

    fun loadWorkers() {
        app.workersRepository.loadWorkers()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { onStartLoading() }
            .subscribe(this::onWorkersLoaded, this::onError)
            .addTo(asyncWorkers)
        Log.i("!!!", "LoadWorkers")
    }

    override fun onCleared() {
        asyncWorkers.clear()
    }

    private fun onWorkersLoaded(workers: List<Worker>) {
        val newUiState = currentUiState.copy(
            workers = workers,
            error = null,
            isLoading = false
        )
        uiState.value = newUiState
    }

    private fun onError(throwable: Throwable) {
        val newUiState = currentUiState.copy(
            workers = emptyList(),
            error = throwable,
            isLoading = false
        )
        uiState.value = newUiState
    }

    private fun onStartLoading() {
        val newUiState = currentUiState.copy(
            isLoading = true
        )
        uiState.value = newUiState
    }

    private fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
        compositeDisposable.add(this)
    }

}

data class WorkersUiState(
        val workers: List<Worker>,
        val error: Throwable?,
        val isLoading: Boolean
)

class DepartmentPage(val workers: List<Worker>) {

    fun getSeparatedWorkersByDepartment(departmentType: DepartmentType): List<Worker>? {

        val groupedList = workers.groupBy { worker ->
            worker.department
        }

        return when (departmentType) {
            DepartmentType.All -> workers
            DepartmentType.Android -> groupedList["android"]
            DepartmentType.IOS -> groupedList["ios"]
            DepartmentType.Design -> groupedList["design"]
            DepartmentType.Management -> groupedList["management"]
            DepartmentType.QA -> groupedList["qa"]
            DepartmentType.BackOffice -> groupedList["back_office"]
            DepartmentType.Backend -> groupedList["backend"]
            DepartmentType.Frontend -> groupedList["frontend"]
            DepartmentType.HR -> groupedList["hr"]
            DepartmentType.PR -> groupedList["pr"]
            DepartmentType.Support -> groupedList["support"]
            DepartmentType.Analytics -> groupedList["analytics"]
        }
    }

}