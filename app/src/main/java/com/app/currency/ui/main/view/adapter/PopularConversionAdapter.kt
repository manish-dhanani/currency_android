package com.app.currency.ui.main.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.currency.data.local.ConversionData
import com.app.currency.databinding.ListItemPopularConversionBinding

class PopularConversionAdapter(private val dataList: List<ConversionData>) :
    RecyclerView.Adapter<PopularConversionAdapter.ViewHolder>() {

    class ViewHolder(val dataBinding: ListItemPopularConversionBinding) :
        RecyclerView.ViewHolder(dataBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPopularConversionBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conversionData = dataList[position]
        holder.dataBinding.conversionData = conversionData
    }

    override fun getItemCount() = dataList.size

}