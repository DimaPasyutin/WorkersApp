package com.example.kode_testapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kode_testapp.R
import com.example.kode_testapp.databinding.ItemWorkerBinding
import com.example.kode_testapp.retrofit.Worker

typealias Navigator = (String) -> Unit

class WorkersListAdapter(private val navigator: Navigator): RecyclerView.Adapter<WorkerViewHolder>(), View.OnClickListener {

    var workerList: List<Worker> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkerBinding.bind(layoutInflater.inflate(R.layout.item_worker, parent, false))
        binding.item.setOnClickListener(this)
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

    override fun onClick(v: View) {
        val worker = v.tag as Worker
        navigator.invoke(worker.id)
    }

}

class WorkerViewHolder(
    val bindingWorker: ItemWorkerBinding
) : RecyclerView.ViewHolder(bindingWorker.root)

