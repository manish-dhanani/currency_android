package com.app.currency.ui.main.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatTextView
import com.app.currency.R
import com.app.currency.data.local.Symbol

class SymbolAdapter(context: Context, private val symbols: List<Symbol>) :
    ArrayAdapter<Symbol>(context, 0, symbols) {

    private class ViewHolder(view: View) {
        var tvCode: AppCompatTextView? = null
        var tvCurrency: AppCompatTextView? = null

        init {
            tvCode = view.findViewById(R.id.tv_code)
            tvCurrency = view.findViewById(R.id.tv_currency)
        }
    }

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount() = symbols.size

    override fun getItem(position: Int) = symbols[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val holder: ViewHolder

        when (convertView) {
            null -> {
                view = inflater.inflate(R.layout.list_item_symbol_dropdown, parent, false)
                holder = ViewHolder(view)
                view?.tag = holder
            }
            else -> {
                view = convertView
                holder = view.tag as ViewHolder
            }
        }

        val symbol = symbols[position]
        holder.apply {
            tvCode?.text = symbol.code
            tvCurrency?.text = symbol.currency
        }

        return view
    }

}