package com.example.kode_testapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kode_testapp.R
import com.example.kode_testapp.databinding.ItemWorkerBinding
import com.example.kode_testapp.retrofit.Worker

class WorkersListAdapter(private val listener: Listener): ListAdapter<Worker, WorkerViewHolder>(WorkerDiffer()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkerBinding.bind(layoutInflater.inflate(R.layout.item_worker, parent, false))
        return WorkerViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: WorkerViewHolder, position: Int) {
        val worker = currentList[position]
        holder.bindWorker(worker)
    }

    fun interface Listener {
        fun onWorkerClick(worker: Worker)
    }
}

class WorkerViewHolder(
    private val bindingWorker: ItemWorkerBinding,
    private val listener: WorkersListAdapter.Listener
) : RecyclerView.ViewHolder(bindingWorker.root) {
    fun bindWorker(worker: Worker) {
        with(bindingWorker) {
            Glide.with(imageViewIconWorker.context)
                .load(worker.avatarUrl)
                .circleCrop()
                .into(imageViewIconWorker)
            textViewNameWorker.text = worker.firstName
            textViewTegWorker.text = worker.userTag
            textViewDepartmentWorker.text = worker.departmentType.toString()
        }

        itemView.setOnClickListener { listener.onWorkerClick(worker) }
    }
}

class WorkerDiffer : DiffUtil.ItemCallback<Worker>() {
    override fun areItemsTheSame(oldItem: Worker, newItem: Worker): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Worker, newItem: Worker): Boolean {
        return oldItem == newItem
    }
}