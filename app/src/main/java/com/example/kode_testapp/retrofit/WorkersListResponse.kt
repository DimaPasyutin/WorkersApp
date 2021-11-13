package com.example.kode_testapp.retrofit

import com.google.gson.annotations.SerializedName

data class WorkersListResponse(@SerializedName("items") val worker: List<Worker>)

data class Worker(

        @SerializedName("id")
        val id: String,

        @SerializedName("avatarUrl")
        val avatarUrl: String,

        @SerializedName("firstName")
        val firstName: String,

        @SerializedName("lastName")
        val lastName: String,

        @SerializedName("userTag")
        val userTag: String,

        @SerializedName("department")
        val department: String,

        @SerializedName("position")
        val position: String,

        @SerializedName("birthday")
        val birthday: String,

        @SerializedName("phone")
        val phone: String)