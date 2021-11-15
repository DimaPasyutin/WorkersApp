package com.example.kode_testapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kode_testapp.R
import com.example.kode_testapp.databinding.ItemWorkersListBinding
import com.example.kode_testapp.retrofit.Worker
import com.example.kode_testapp.screens.main_page.DepartmentPage

class ViewPagerAdapter(private val listener: Listener) : ListAdapter<DepartmentPage, ViewPagerAdapter.WorkersListHolder>(DepartmentPageDiffer()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkersListHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkersListBinding.bind(layoutInflater.inflate(R.layout.item_workers_list, parent, false))
        return  WorkersListHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: WorkersListHolder, position: Int) {
        holder.bindPage(currentList[position])
    }

    inner class WorkersListHolder(
        private val bindingWorkersList: ItemWorkersListBinding,
        private val listener: Listener
    ): RecyclerView.ViewHolder(bindingWorkersList.root) {
        fun bindPage(page: DepartmentPage) {
            val workersListAdapter = WorkersListAdapter { listener.onWorkerClick(it) }
            val layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
            bindingWorkersList.recyclerView.layoutManager = layoutManager
            bindingWorkersList.recyclerView.adapter = workersListAdapter

            bindingWorkersList.swipeContainer.setOnRefreshListener {
                listener.onRefresh(page)
            }
            bindingWorkersList.swipeContainer.isRefreshing = page.refreshing

            workersListAdapter.submitList(page.workers)
        }
    }

    interface Listener {
        fun onRefresh(page: DepartmentPage)

        fun onWorkerClick(worker: Worker)
    }
}

class DepartmentPageDiffer : DiffUtil.ItemCallback<DepartmentPage>() {
    override fun areItemsTheSame(oldItem: DepartmentPage, newItem: DepartmentPage): Boolean {
        return oldItem.department.departmentType == newItem.department.departmentType
    }

    override fun areContentsTheSame(oldItem: DepartmentPage, newItem: DepartmentPage): Boolean {
       return oldItem == newItem
    }
}
