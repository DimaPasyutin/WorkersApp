package com.example.kode_testapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kode_testapp.R
import com.example.kode_testapp.databinding.ItemWorkersListBinding
import com.example.kode_testapp.pojo.Department
import com.example.kode_testapp.pojo.DepartmentType
import com.example.kode_testapp.retrofit.Worker
import com.example.kode_testapp.screens.main_page.DepartmentPage

class ViewPagerAdapter(val departmentPage: DepartmentPage) : RecyclerView.Adapter<WorkersListHolder>() {

    var departments: List<Department> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkersListHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkersListBinding.bind(layoutInflater.inflate(R.layout.item_workers_list, parent, false))
        return  WorkersListHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkersListHolder, position: Int) {
        when (departments[position].departmentType) {
            DepartmentType.All -> holder.createRecyclerView(holder.itemView.context, departmentPage.getSeparatedWorkersByDepartment(DepartmentType.All))
            DepartmentType.Android -> holder.createRecyclerView(holder.itemView.context, departmentPage.getSeparatedWorkersByDepartment(DepartmentType.Android))
            DepartmentType.IOS -> holder.createRecyclerView(holder.itemView.context, departmentPage.getSeparatedWorkersByDepartment(DepartmentType.IOS))
            DepartmentType.Design -> holder.createRecyclerView(holder.itemView.context, departmentPage.getSeparatedWorkersByDepartment(DepartmentType.Design))
            DepartmentType.Management -> holder.createRecyclerView(holder.itemView.context, departmentPage.getSeparatedWorkersByDepartment(DepartmentType.Management))
            DepartmentType.QA -> holder.createRecyclerView(holder.itemView.context, departmentPage.getSeparatedWorkersByDepartment(DepartmentType.QA))
            DepartmentType.BackOffice -> holder.createRecyclerView(holder.itemView.context, departmentPage.getSeparatedWorkersByDepartment(DepartmentType.BackOffice))
            DepartmentType.Backend -> holder.createRecyclerView(holder.itemView.context, departmentPage.getSeparatedWorkersByDepartment(DepartmentType.Backend))
            DepartmentType.Frontend -> holder.createRecyclerView(holder.itemView.context, departmentPage.getSeparatedWorkersByDepartment(DepartmentType.Frontend))
            DepartmentType.HR -> holder.createRecyclerView(holder.itemView.context, departmentPage.getSeparatedWorkersByDepartment(DepartmentType.HR))
            DepartmentType.PR -> holder.createRecyclerView(holder.itemView.context, departmentPage.getSeparatedWorkersByDepartment(DepartmentType.PR))
            DepartmentType.Support -> holder.createRecyclerView(holder.itemView.context, departmentPage.getSeparatedWorkersByDepartment(DepartmentType.Support))
            DepartmentType.Analytics -> holder.createRecyclerView(holder.itemView.context, departmentPage.getSeparatedWorkersByDepartment(DepartmentType.Analytics))
        }
    }

    override fun getItemCount(): Int {
        return departments.size
    }

}

class WorkersListHolder(
    val bindingWorkersList: ItemWorkersListBinding
    ): RecyclerView.ViewHolder(bindingWorkersList.root) {

        fun createRecyclerView(context: Context ,workerList: List<Worker>?) {
            val workersListAdapter = WorkersListAdapter()
            if (workerList !== null) {
                workersListAdapter.workerList = workerList
                val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                bindingWorkersList.recyclerView.layoutManager = layoutManager
                bindingWorkersList.recyclerView.adapter = workersListAdapter
            }
        }

    }