package com.example.kode_testapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kode_testapp.R
import com.example.kode_testapp.databinding.ItemWorkerBinding
import com.example.kode_testapp.retrofit.Worker

class WorkersListAdapter(): RecyclerView.Adapter<WorkerViewHolder>() {

    var workerList: List<Worker> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkerBinding.bind(layoutInflater.inflate(R.layout.item_worker, parent, false))
        return  WorkerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkerViewHolder, position: Int) {

        val worker = workerList[position]
        holder.itemView.tag = worker
        with(holder.bindingWorker) {
            Glide.with(imageViewIconWorker.context)
                .load(worker.avatarUrl)
                .circleCrop()
                .into(imageViewIconWorker)
            textViewNameWorker.text = worker.firstName
            textViewTegWorker.text = worker.userTag
            textViewDepartmentWorker.text = worker.department
        }
    }

    override fun getItemCount(): Int {
        return workerList.size
    }

}

class WorkerViewHolder(
    val bindingWorker: ItemWorkerBinding
) : RecyclerView.ViewHolder(bindingWorker.root)

