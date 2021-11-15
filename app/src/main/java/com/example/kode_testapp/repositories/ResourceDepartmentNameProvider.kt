package com.example.kode_testapp.repositories

import android.content.Context
import com.example.kode_testapp.R
import com.example.kode_testapp.pojo.DepartmentType

class ResourceDepartmentNameProvider(private val appContext: Context) : DepartmentNameProvider {

    override fun provideName(departmentType: DepartmentType): String {
        return when (departmentType) {
            DepartmentType.All -> appContext.getString(R.string.All)
            DepartmentType.Android -> appContext.getString(R.string.Android)
            DepartmentType.IOS -> appContext.getString(R.string.IOS)
            DepartmentType.Design -> appContext.getString(R.string.Design)
            DepartmentType.Management -> appContext.getString(R.string.Management)
            DepartmentType.QA -> appContext.getString(R.string.QA)
            DepartmentType.BackOffice -> appContext.getString(R.string.BackOffice)
            DepartmentType.Frontend -> appContext.getString(R.string.Frontend)
            DepartmentType.HR -> appContext.getString(R.string.HR)
            DepartmentType.PR -> appContext.getString(R.string.PR)
            DepartmentType.Backend -> appContext.getString(R.string.Backend)
            DepartmentType.Support -> appContext.getString(R.string.Support)
            DepartmentType.Analytics -> appContext.getString(R.string.Analytics)
        }
    }

}