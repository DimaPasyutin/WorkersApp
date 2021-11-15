package com.example.kode_testapp.retrofit

import com.example.kode_testapp.pojo.DepartmentType
import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

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
    @JsonAdapter(DepartmentTypeConverter::class)
    val departmentType: DepartmentType,

    @SerializedName("position")
    val position: String,

    @SerializedName("birthday")
    val birthday: String,

    @SerializedName("phone")
    val phone: String
)

class DepartmentTypeConverter : TypeAdapter<DepartmentType>() {
    override fun write(out: JsonWriter, value: DepartmentType) {
        out.value(value.name.toLowerCase())
    }

    override fun read(reader: JsonReader): DepartmentType {
        return when (val value = reader.nextString().toLowerCase()) {
            "android" -> DepartmentType.Android
            "ios" -> DepartmentType.IOS
            "design" -> DepartmentType.Design
            "management" -> DepartmentType.Management
            "qa" -> DepartmentType.QA
            "back_office" -> DepartmentType.BackOffice
            "frontend" -> DepartmentType.Frontend
            "hr" -> DepartmentType.HR
            "pr" -> DepartmentType.PR
            "backend" -> DepartmentType.Backend
            "support" -> DepartmentType.Support
            "analytics" -> DepartmentType.Analytics
            else -> throw IllegalArgumentException("type = $value not supported")
        }
    }
}