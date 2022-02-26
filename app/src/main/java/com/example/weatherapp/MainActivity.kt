package com.example.weatherapp

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class MainActivity : AppCompatActivity() {

    private val apiKey = "a12bec12393f5199d393a0108ff06758"
    private lateinit var api : Api
    private lateinit var myConstraintLayout: ConstraintLayout
    private lateinit var animationDrawable: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //set animation
        myConstraintLayout = findViewById(R.id.ConstraintLayout_main)
        animationDrawable = myConstraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(15)
        animationDrawable.setExitFadeDuration(3000)
        animationDrawable.start()

        //Set button
        button.setOnClickListener {
            startActivity(Intent(this, ForecastActivity::class.java))
        }

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(Api::class.java)

    }

    override fun onResume() {
        super.onResume()

        val call: Call<CurrentConditions> = api.getCurrentCondition("55016")
        call.enqueue(object: Callback<CurrentConditions>{
            override fun onResponse(
                call: Call<CurrentConditions>,
                response: Response<CurrentConditions>
            ) {
                val currentConditions = response.body()
                currentConditions?.let {
                    bindData(it)
                }
            }
            override fun onFailure(call: Call<CurrentConditions>, t: Throwable) {
            }

        })
    }

    private fun bindData(currentConditions: CurrentConditions){
        city_name.text = currentConditions.name
        temperature.text = getString(R.string.temperature, currentConditions.main.temp.toInt())
        feel_like.text = getString(R.string.feel_like, currentConditions.main.feelsLike.toInt())
        temp_low.text = getString(R.string.current_low, currentConditions.main.tempMin.toInt())
        temp_high.text = getString(R.string.current_high, currentConditions.main.tempMax.toInt())
        temp_humidity.text = getString(R.string.current_humidity, currentConditions.main.humidity.toInt())
        temp_pressure.text = getString(R.string.current_pressure, currentConditions.main.pressure.toInt())

        val iconName = currentConditions.weather.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(icon)

    }
}