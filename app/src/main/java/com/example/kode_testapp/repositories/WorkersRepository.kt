package com.example.kode_testapp.repositories

import com.example.kode_testapp.retrofit.Worker
import com.example.kode_testapp.retrofit.WorkersApi
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class WorkersRepository(private val workersApi: WorkersApi) {

    @Volatile
    var workersInMemoryCache: List<Worker> = ArrayList()

    fun loadWorkers(): Single<List<Worker>> {
        return workersApi.getWorkerList()
            .map { it.worker }
            .doOnSuccess { saveWorkers(it) }
            .subscribeOn(Schedulers.io())
    }

    @Synchronized
    private fun saveWorkers(newWorkers: List<Worker>) {
        if (workersInMemoryCache == newWorkers) return
        workersInMemoryCache = newWorkers
    }

}