package com.ibrahim.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ibrahim.home.databinding.ItemRateBinding

class ExchangeRatesAdapter(
    val items: MutableList<ExchangeRate> = mutableListOf(),
    val onItemCLick: (exchangeRate: ExchangeRate) -> Unit
) :
    RecyclerView.Adapter<ExchangeRatesAdapter.RateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        return RateViewHolder(
            ItemRateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        with(holder.itemRateBinding) {
            val rate = items[position]
            root.setOnClickListener { onItemCLick(rate) }
            tvName.text = rate.name
            tvRateValue.text = rate.rate.toString()
        }
    }

    inner class RateViewHolder(val itemRateBinding: ItemRateBinding) :
        RecyclerView.ViewHolder(itemRateBinding.root)

}