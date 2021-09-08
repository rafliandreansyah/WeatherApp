package com.example.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.data.source.remote.entity.Daily
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.ui.adapter.UpcomingAdapter
import com.example.weatherapp.utlis.Helper
import com.example.weatherapp.utlis.Resource
import com.example.weatherapp.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var upcomingAdapter: UpcomingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        (application as WeatherApplication).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val listTown = arrayListOf("Gdansk", "Warszawa", "Krakow", "Wroclaw", "Lodz")

        val adapter = ArrayAdapter(this, R.layout.list_item, listTown)
        (binding.acCity as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.acCity.setOnItemClickListener { adapterView, view, i, l ->
            getData(adapter.getItem(i))
        }


        binding.refresh.setOnRefreshListener {
            getData(binding.acCity.text.toString())
        }

    }


    private fun getData(town: String?){
        val data = town?.let { mainViewModel.getWeatherByCity(it) }
        data?.observe(this,  { data ->
            Log.e("Activity", data.data.toString())
            when(data){
                is Resource.Success -> {
                    binding.tvHumidity.text = "${data.data?.main?.humidity}%"
                    binding.tvPressure.text = "${data.data?.main?.pressure} hPa"
                    binding.tvTemp.text = "${data.data?.main?.temp?.toInt()}\u2103"
                    binding.tvDate.text = "${data.data?.dt?.toLong()?.let {
                        Helper.convertDateFullFormat(
                            it
                        )
                    }}"
                    Glide.with(this)
                        .load("http://openweathermap.org/img/wn/${data.data?.weather?.get(0)?.icon}@2x.png")
                        .into(binding.imgCloud)

                    binding.tvDescriptionWeather.text = "${data.data?.weather?.get(0)?.description?.capitalize()}"
                    getWeatherByLotLan("${data.data?.coord?.lat}", "${data.data?.coord?.lon}")
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

    private fun getWeatherByLotLan(lat: String, lon: String){
        val data = mainViewModel.getWeather(lat, lon)
        data.observe(this, { data ->
            when(data){
                is Resource.Success -> {
                    upcomingAdapter.setData(data.data?.daily as ArrayList<Daily>)

                    with(binding){

                        rvUpComingDays.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                        rvUpComingDays.adapter = upcomingAdapter
                        rvUpComingDays.setHasFixedSize(true)

                        loading.visibility = View.INVISIBLE
                        refresh.isRefreshing = false

                    }
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