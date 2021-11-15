package com.example.kode_testapp

import android.app.Application
import com.example.kode_testapp.pojo.Department
import com.example.kode_testapp.repositories.DepartmentStorage
import com.example.kode_testapp.repositories.ResourceDepartmentNameProvider
import com.example.kode_testapp.repositories.WorkersRepository
import com.example.kode_testapp.retrofit.WorkersApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class App: Application() {

    lateinit var workersApi: WorkersApi
    lateinit var departmentStorage: DepartmentStorage

    var departmentList = emptyList<Department>()

    lateinit var workersRepository: WorkersRepository

    override fun onCreate() {
        super.onCreate()
        configureRetrofit()
        initDepartmentList()

        workersRepository = WorkersRepository(workersApi)
    }

    private fun configureRetrofit() {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl("https://stoplight.io/mocks/kode-education/trainee-test/25143926/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        workersApi = retrofit.create(WorkersApi::class.java)
    }

    private fun initDepartmentList() {
        val resourceDepartmentNameProvider = ResourceDepartmentNameProvider(applicationContext)
        departmentStorage = DepartmentStorage(resourceDepartmentNameProvider)
        departmentList = departmentStorage.getDepartments()
    }

}