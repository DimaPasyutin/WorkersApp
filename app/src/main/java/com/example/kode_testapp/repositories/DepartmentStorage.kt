package com.example.kode_testapp.repositories

import com.example.kode_testapp.pojo.Department
import com.example.kode_testapp.pojo.DepartmentType

class DepartmentStorage(private val departmentNameProvider: DepartmentNameProvider) {

    private var departments: List<Department> = emptyList()
    private var currentPosition = 0

    fun getDepartments(): List<Department> {
        initDepartments()
        return departments
    }

    private fun initDepartments() {
        if (departments.isNotEmpty()) return

        departments = DepartmentType.values().map { departmentType ->
            Department(
                name = departmentNameProvider.provideName(departmentType),
                position = currentPosition++,
                departmentType = departmentType
            )
        }
    }

}

interface DepartmentNameProvider {
    fun provideName(departmentType: DepartmentType): String
}