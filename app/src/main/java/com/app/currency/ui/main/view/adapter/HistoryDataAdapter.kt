package com.app.currency.ui.main.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.currency.data.local.ConversionData
import com.app.currency.databinding.ListItemHistoryDataBinding

class HistoryDataAdapter(private val dataList: List<ConversionData>) :
    RecyclerView.Adapter<HistoryDataAdapter.ViewHolder>() {

    class ViewHolder(val dataBinding: ListItemHistoryDataBinding) :
        RecyclerView.ViewHolder(dataBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemHistoryDataBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val historyData = dataList[position]
        holder.dataBinding.historyData = historyData
    }

    override fun getItemCount() = dataList.size

}