package com.example.kode_testapp.pojo



class Department (
val name: String,
val position: Int,
val departmentType: DepartmentType
)

enum class DepartmentType {
    All,
    Android,
    IOS,
    Design,
    Management,
    QA,
    BackOffice,
    Frontend,
    HR,
    PR,
    Backend,
    Support,
    Analytics
}