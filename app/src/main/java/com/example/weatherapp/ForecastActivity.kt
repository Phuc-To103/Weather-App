package com.example.weatherapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


class ForecastActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var api: Api


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "Forecast"
        val backgroundColor = ColorDrawable(Color.parseColor("#FFA500"))
        actionBar?.setBackgroundDrawable(backgroundColor)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setBackgroundColor(Color.parseColor("#FF03DAC5"))
        recyclerView.adapter = MyAdapter(listOf<DateForecast>())

        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/forecast/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        api = retrofit.create(Api::class.java)
    }

    override fun onResume() {
        super.onResume()
        val call: Call<Forecast> = api.getForecast("55016")
        call.enqueue(object : Callback<Forecast> {
            override fun onResponse(
                call: Call<Forecast>,
                response: Response<Forecast>
            ) {
                val foreCast = response.body()
                foreCast?.let {
                    bindData(it)
                }
            }

            override fun onFailure(call: Call<Forecast>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }


    private fun bindData(foreCast: Forecast) {
        recyclerView.adapter = MyAdapter(foreCast.list)
    }

}