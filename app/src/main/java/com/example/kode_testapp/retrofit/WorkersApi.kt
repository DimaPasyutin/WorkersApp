package com.example.kode_testapp.retrofit

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers

interface WorkersApi {

    @GET("./users")
    @Headers("Content-Type:application/json")
    fun getWorkerList(): Single<WorkersListResponse>

}