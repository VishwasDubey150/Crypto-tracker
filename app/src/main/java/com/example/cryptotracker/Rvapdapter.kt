package com.example.cryptotracker

import android.content.Context
import android.graphics.ColorSpace
import android.graphics.ColorSpace.Model
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptotracker.databinding.RvItemBinding

class Rvapdapter(val context: Context,var data:ArrayList<model>) : RecyclerView.Adapter<Rvapdapter.ViewHolder>() {

    inner class ViewHolder(val binding : RvItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=RvItemBinding.inflate(LayoutInflater.from(context),parent,false)
         return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.name.text=data[position].name
        holder.binding.price.text=data[position].price
    }

    override fun getItemCount(): Int {
        return data.size
    }
}