package com.example.weatherapp.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.ListUpcomingBinding
import javax.inject.Inject

class UpcomingAdapter @Inject constructor(): RecyclerView.Adapter<UpcomingAdapter.UpCommingViewHolder>() {

    fun setData(){

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpcomingAdapter.UpCommingViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: UpcomingAdapter.UpCommingViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class UpCommingViewHolder(private val binding: ListUpcomingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(){

        }
    }
}