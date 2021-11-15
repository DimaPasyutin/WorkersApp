package com.example.kode_testapp.screens.main_page

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kode_testapp.App
import com.example.kode_testapp.pojo.Department
import com.example.kode_testapp.pojo.DepartmentType
import com.example.kode_testapp.retrofit.Worker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class MainPageViewModel(val app: App) : ViewModel() {

    private val uiState = MutableLiveData(WorkersUiState(emptyList(), null, false, false, true))
    val uiStateChanges: LiveData<WorkersUiState> = uiState

    private val currentUiState: WorkersUiState
        get() = requireNotNull(uiState.value)

    private val asyncWorkers = CompositeDisposable()

    private val workersRepository = app.workersRepository
    private val departmentsStorage = app.departmentStorage

    val departments: List<Department> = departmentsStorage.getDepartments()
        .sortedBy { it.departmentType.ordinal }

    private var allPages: List<DepartmentPage> = emptyList()

    fun loadWorkers() {
        onStartLoading()
        fetchWorkers()
    }

    fun refresh(page: DepartmentPage) {
        val newPages = markPageRefreshing(page)
        updateState {
            copy(
                pages = newPages,
                isLoading = false,
                isRefreshing = true
            )
        }
        fetchWorkers()
    }

    private fun fetchWorkers() {
        workersRepository.loadWorkers()
            .map { configurePages(departments, it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onPagesUpdate, this::onError)
            .addTo(asyncWorkers)
    }

    override fun onCleared() {
        asyncWorkers.clear()
    }

    private fun configurePages(
        departments: List<Department>,
        workers: List<Worker>
    ): List<DepartmentPage> {
        return workers
            .groupBy { it.departmentType }
            .mapKeys { extractDepartment(departments, it.key) }
            .toList()
            .map {
                DepartmentPage(
                    it.first,
                    it.second,
                    false
                )
            }
            .sortedBy { it.department.departmentType.ordinal }
            .toMutableList()
            .apply {
                add(
                    0,
                    DepartmentPage(
                        extractDepartment(departments, DepartmentType.All),
                        workers,
                        false
                    )
                )
            }
    }

    private fun extractDepartment(
        departments: List<Department>,
        departmentType: DepartmentType
    ): Department {
        return departments.first { it.departmentType == departmentType }
    }

    private fun markPageRefreshing(refreshingPage: DepartmentPage): List<DepartmentPage> {
        val pages = currentUiState.pages.toMutableList()
        val iterator = pages.listIterator()
        while (iterator.hasNext()) {
            val page = iterator.next()
            if (page.department.departmentType == refreshingPage.department.departmentType) {
                iterator.set(page.copy(refreshing = true))
                return pages
            }
        }
        return pages
    }

    private fun onPagesUpdate(pages: List<DepartmentPage>) {
        allPages = pages
        updateState {
            copy(
                pages = pages,
                error = null,
                isLoading = false,
                isRefreshing = false
            )
        }
    }

    private fun onError(throwable: Throwable) {
        val pages = when {
            currentUiState.isRefreshing -> currentUiState.pages
            else -> emptyList()
        }
        updateState {
            copy(
                pages = pages,
                error = throwable,
                isLoading = false,
                isRefreshing = false
            )
        }
    }

    private fun onStartLoading() {
        updateState {
            copy(
                isLoading = true,
                isRefreshing = false
            )
        }
    }

    fun search(searchPage: DepartmentPage, searchText: String) {
        if (searchText.isEmpty()) {
            updateState {
                copy(
                    pages = allPages,
                    isSearchSucces = true
                )
            }
            return
        }

        val searchedWorkers = searchPage.workers.filter {
            filterSearch(searchText, it)
        }

        if (searchedWorkers.isEmpty()) {
            updateState {
                copy(
                    pages = emptyList(),
                    isSearchSucces = false
                )
            }
            return
        }

        val newPages = changeWorkersInPage(searchPage, searchedWorkers)

        updateState {
            copy(
                pages = newPages
            )
        }
    }

    private fun changeWorkersInPage(
        changePage: DepartmentPage,
        workers: List<Worker>
    ): List<DepartmentPage> {
        val pages = allPages.toMutableList()
        val iterator = pages.listIterator()
        while (iterator.hasNext()) {
            val page = iterator.next()
            if (page.department.departmentType == changePage.department.departmentType) {
                iterator.set(page.copy(workers = workers))
                break
            }
        }
        return pages
    }

    private fun filterSearch(searchText: String, worker: Worker): Boolean {
        return worker.firstName.contains(searchText) || worker.lastName.contains(searchText) || worker.userTag.contains(
            searchText
        )
    }

    private fun updateState(mutate: WorkersUiState.() -> WorkersUiState) {
        val newState = mutate(currentUiState)
        if (newState != currentUiState) {
            uiState.value = newState
        }
    }

    private fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
        compositeDisposable.add(this)
    }
}

data class WorkersUiState(
    val pages: List<DepartmentPage>,
    val error: Throwable?,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val isSearchSucces: Boolean
)

data class DepartmentPage(
    val department: Department,
    val workers: List<Worker>,
    val refreshing: Boolean
)