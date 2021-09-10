package com.example.weatherapp.ui

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
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
import com.example.weatherapp.databinding.DialogAddCityBinding
import com.example.weatherapp.ui.adapter.UpcomingAdapter
import com.example.weatherapp.utlis.Helper
import javax.inject.Inject
import android.R.string.no
import android.graphics.Rect
import android.view.WindowManager


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listTown: ArrayList<String>

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

        // Get city from local
        mainViewModel.getAllCityLocal()

        listTown = arrayListOf("Gdansk", "Warszawa", "Krakow", "Wroclaw", "Lodz")

        val adapter = ArrayAdapter(this, R.layout.list_item, listTown)
        (binding.acCity as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.acCity.setOnItemClickListener { adapterView, view, i, l ->
            adapter.getItem(i)?.let { mainViewModel.getWeatherByCity(it) }
        }


        binding.refresh.setOnRefreshListener {
            mainViewModel.getWeatherByCity(binding.acCity.text.toString())
        }

        binding.btnAddTown.setOnClickListener {
            dialogAddTown()
        }

        setDataWeatherByCity()
        setDataListWeatherFor7Days()
        isLoading()
        errorGetData()
        setDataCity()

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

    private fun dialogAddTown(){
        val dialog = Dialog(this)

        val dialogBinding = DialogAddCityBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        val textWatcher = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.length == 0){
                    dialogBinding.btnDialogAddTown.isEnabled = false
                }else{
                    dialogBinding.btnDialogAddTown.isEnabled = dialogBinding.edtTown.text.toString() != ""
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }

        dialogBinding.edtTown.addTextChangedListener(textWatcher)

        dialogBinding.btnDialogAddTown.setOnClickListener {
            val cityFound = listTown.filter { city ->
                city.uppercase() == dialogBinding.edtTown.text.toString().uppercase()
            }
            if (cityFound.isNotEmpty()){
                Toast.makeText(this, "Cannot same city added!", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            else{
                mainViewModel.insertDataCity(dialogBinding.edtTown.text.toString())
                listTown.add(dialogBinding.edtTown.text.toString().capitalize())
                dialog.dismiss()
            }
        }

        dialog.show()

    }

    private fun setDataCity(){
        mainViewModel.dataCityFromLocal.observe(this, { listCity ->
            if (listCity.size > 0){
                listTown.addAll(listCity)
            }
        })
    }



}