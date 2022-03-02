package com.example.weatherapp

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    @Inject lateinit var viewModel: MainViewModel

    private lateinit var myConstraintLayout: ConstraintLayout
    private lateinit var animationDrawable: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myConstraintLayout = findViewById(R.id.ConstraintLayout_main)
        animationDrawable = myConstraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(15)
        animationDrawable.setExitFadeDuration(3000)
        animationDrawable.start()


        binding.button.setOnClickListener {
            startActivity(Intent(this, ForecastActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.currentConditions.observe(this){
                currentConditions -> bindData(currentConditions)
        }
        viewModel.loadData()
    }

    private fun bindData(currentConditions: CurrentConditions){
        binding.cityName.text = currentConditions.name
        binding.temperature.text = getString(R.string.temperature, currentConditions.main.temp.toInt())
        binding.feelsLike.text = getString(R.string.feel_like, currentConditions.main.feelsLike.toInt())
        binding.lowTemp.text = getString(R.string.current_low, currentConditions.main.tempMin.toInt())
        binding.highTemp.text = getString(R.string.current_high, currentConditions.main.tempMax.toInt())
        binding.huminityTemp.text = getString(R.string.current_humidity, currentConditions.main.humidity.toInt())
        binding.pressureTemp.text = getString(R.string.current_pressure, currentConditions.main.pressure.toInt())

        val iconName = currentConditions.weather.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(binding.iconTemp)

    }
}