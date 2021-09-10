package com.example.weatherapp.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.data.source.remote.entity.Daily
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.ui.adapter.UpcomingAdapter
import com.example.weatherapp.utlis.Helper
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
            adapter.getItem(i)?.let { mainViewModel.getWeatherByCity(it) }
        }


        binding.refresh.setOnRefreshListener {
            mainViewModel.getWeatherByCity(binding.acCity.text.toString())
        }

        setDataWeatherByCity()
        setDataListWeatherFor7Days()
        isLoading()
        errorGetData()

    }

    private fun setDataWeatherByCity(){
        mainViewModel.weatherResponseByCity.observe(this, { data ->
            binding.tvHumidity.text = "${data?.main?.humidity}%"
            binding.tvPressure.text = "${data?.main?.pressure} hPa"
            binding.tvTemp.text = "${data?.main?.temp?.toInt()}\u2103"
            binding.tvDate.text = "${data?.dt?.toLong()?.let {
                Helper.convertDateFullFormat(
                    it
                )
            }}"
            Glide.with(this)
                .load("http://openweathermap.org/img/wn/${data?.weather?.get(0)?.icon}@2x.png")
                .into(binding.imgCloud)

            binding.tvDescriptionWeather.text = "${data?.weather?.get(0)?.description?.capitalize()}"
        })
    }

    private fun setDataListWeatherFor7Days(){
        mainViewModel.weatherResponse.observe(this, { data ->
            val weatherResponse = data?.copy()
            val daily = weatherResponse?.daily as ArrayList<Daily>
            daily.removeFirst()
            upcomingAdapter.setData(daily)

            with(binding){

                rvUpComingDays.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                rvUpComingDays.adapter = upcomingAdapter
                rvUpComingDays.setHasFixedSize(true)

                loading.visibility = View.INVISIBLE
                refresh.isRefreshing = false

            }
        })
    }

    private fun isLoading(){
        mainViewModel.isLoading.observe(this, { loading ->
            if (loading == true){
                binding.loading.visibility = View.VISIBLE
            }
            else {
                binding.loading.visibility = View.INVISIBLE
            }
        })
    }

    private fun errorGetData(){
        mainViewModel.errorMessage.observe(this, { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        })
    }



}