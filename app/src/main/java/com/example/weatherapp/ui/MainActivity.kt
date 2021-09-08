package com.example.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.ui.adapter.UpcomingAdapter
import com.example.weatherapp.utlis.Resource
import com.example.weatherapp.viewmodel.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModelFactory: ViewModelFactory
    private lateinit var mainViewModel: MainViewModel

    lateinit var upcomingAdapter: UpcomingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModelFactory = ViewModelFactory.getInstance(applicationContext)!!
        mainViewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        val listTown = arrayListOf("Gdansk", "Warszawa", "Krakow", "Wroclaw", "Lodz")

        val adapter = ArrayAdapter(this, R.layout.list_item, listTown)
        (binding.acCity as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.acCity.setOnItemClickListener { adapterView, view, i, l ->
            setData(adapter.getItem(i))
        }


        binding.refresh.setOnRefreshListener {
            setData(binding.acCity.text.toString())
        }

    }


    private fun setData(town: String?){


        val data = town?.let { mainViewModel.getWeatherByCity(it) }
        data?.observe(this,  { data ->
            when(data){
                is Resource.Success -> {
                    binding.loading.visibility = View.INVISIBLE
                    binding.tvHumidity.text = "${data.data?.main?.humidity}%"
                    binding.tvPressure.text = "${data.data?.main?.pressure} hPa"
                    binding.tvTemp.text = "${data.data?.main?.temp?.toInt()}\u2103"
                    Glide.with(this)
                        .load("http://openweathermap.org/img/wn/${data.data?.weather?.get(0)?.icon}@2x.png")
                        .into(binding.imgCloud)

                    binding.tvDescriptionWeather.text = "${data.data?.weather?.get(0)?.description}"
                    binding.refresh.isRefreshing = false
                }
                is Resource.Loading -> {
                    binding.loading.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.loading.visibility = View.INVISIBLE
                    binding.refresh.isRefreshing = false
                }
            }
        })

    }


}