package com.example.weatherapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.data.source.remote.entity.Daily
import com.example.weatherapp.databinding.ListUpcomingBinding
import com.example.weatherapp.utlis.Helper
import javax.inject.Inject

class UpcomingAdapter @Inject constructor(): RecyclerView.Adapter<UpcomingAdapter.UpCommingViewHolder>() {

    private var listWeather = ArrayList<Daily>()

    fun setData(listWeather: ArrayList<Daily>){
        this.listWeather = listWeather
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UpcomingAdapter.UpCommingViewHolder =
        UpCommingViewHolder(ListUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: UpcomingAdapter.UpCommingViewHolder, position: Int) {
        holder.bind(listWeather[position])
    }

    override fun getItemCount(): Int {
        return listWeather.size
    }

    inner class UpCommingViewHolder(private val binding: ListUpcomingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(daily: Daily){
            binding.tvItemDay.text = "${Helper.convertDate(daily.dt.toLong())}"
            binding.tvItemTemp.text = "${daily.temp}℃"
            Glide.with(itemView)
                .load("http://openweathermap.org/img/wn/${daily.weather[0].icon}@2x.png")
                .into(binding.imgItemCloud)
        }
    }
}