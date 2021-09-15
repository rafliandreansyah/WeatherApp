package com.example.weatherapp.ui

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.weatherapp.databinding.DialogAddCityBinding
import com.example.weatherapp.ui.adapter.UpcomingAdapter
import com.example.weatherapp.utlis.Helper
import javax.inject.Inject
import android.R.string.no
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Rect
import android.location.LocationManager
import android.os.Build
import android.util.Log
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var listTown: ArrayList<String>

    // Get last location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 99
    }

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

        initRequestPermission()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Get city from local
        mainViewModel.getAllCityLocal()

        listTown = arrayListOf("Gdansk", "Warszawa", "Krakow", "Wroclaw", "Lodz")

        val adapter = ArrayAdapter(this, R.layout.list_item, listTown)
        (binding.acCity as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.acCity.setOnItemClickListener { adapterView, view, i, l ->
            adapter.getItem(i)?.let { mainViewModel.getWeatherByCity(it) }
        }


        binding.refresh.setOnRefreshListener {
            if (binding.acCity.text.toString().trim().isEmpty() || binding.acCity.text.toString().trim() == ""){
                checkPermission()
                binding.refresh.isRefreshing = false
            }
            else {
                mainViewModel.getWeatherByCity(binding.acCity.text.toString())
            }

        }

        binding.btnAddTown.setOnClickListener {
            dialogAddTown()
        }

        // Check permission if granted get last location lat, long if denied request permission
        checkPermission()

        // Set weather first time by latitude longitude last location
        setWeatherFirstTime()

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

    private fun setWeatherFirstTime(){
        mainViewModel.weatherResponseFirstTime.observe(this,  { weatherResponse ->
            binding.tvHumidity.text = "${weatherResponse?.current?.humidity}%"
            binding.tvPressure.text = "${weatherResponse?.current?.pressure} hPa"
            binding.tvTemp.text = "${weatherResponse?.current?.temp?.toInt()}\u2103"
            binding.tvDate.text = "${weatherResponse?.current?.dt?.toLong()?.let {
                Helper.convertDateFullFormat(
                    it
                )
            }}"
            Glide.with(this)
                .load("http://openweathermap.org/img/wn/${weatherResponse?.current?.weather?.get(0)?.icon}@2x.png")
                .into(binding.imgCloud)

            binding.tvDescriptionWeather.text = "${weatherResponse?.current?.weather?.get(0)?.description?.capitalize()}"

            // Recycleview
            val weatherResponseData = weatherResponse?.copy()
            val daily = weatherResponseData?.daily as ArrayList<Daily>
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

    @SuppressLint("MissingPermission")
    private fun getLocationFirstTimeByLastLocation(){
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener {
            mainViewModel.getWeatherFirstTime(it.latitude.toString(), it.longitude.toString())

            Log.e("Location", "Lat: ${it.latitude}, Lon: ${it.longitude} ")
        }
    }

    private fun initRequestPermission(){
        // Request permission
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
                isGranted: Boolean ->
            if (isGranted){
                getLocationFirstTimeByLastLocation()
                Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Permission is denied, you cannot get weather by last location!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermission(){
        when{
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED  -> {
                getLocationFirstTimeByLastLocation()
                    }
            else -> {
                requestPermission()
            }
        }
    }


    private fun requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }else{
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


}