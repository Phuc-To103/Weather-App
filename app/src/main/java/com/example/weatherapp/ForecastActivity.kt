package com.example.weatherapp

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_forecast.*


class ForecastActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView

    val tempForecast = listOf<TempForecast>(
        TempForecast(25F, 19F, 28F),
        TempForecast(10F, 5F, 12F),
        TempForecast(3F, -4F, 2F),
        TempForecast(6F, -2F, 9F),
        TempForecast(8F, -2F, 10F),
        TempForecast(15F, 5F, 23F),
        TempForecast(16F, -2F, 21F),
        TempForecast(30F, 18F, 38F),
        TempForecast(26F, 18F, 31F),
        TempForecast(32F, 19F, 37F),
        TempForecast(30F, 28F, 36F),
        TempForecast(7F, 1F, 11F),
        TempForecast(15F, 5F, 20F),
        TempForecast(16F, 10F, 21F),
        TempForecast(26F, 23F, 30F),
        TempForecast(24F, 21F, 27F)
    )

    val adapterData = listOf<DateForecast>(
        DateForecast(1643757394, 1643722294, 1643757634, tempForecast[0], 90, 30F),
        DateForecast(1643844094, 1643808634, 1643844094, tempForecast[1], 84, 28.63F),
        DateForecast(1643894914, 1643894914, 1643930554, tempForecast[2], 86, 29.88F),
        DateForecast(1643981254, 1643981254, 1644017074, tempForecast[3], 91, 29.77F),
        DateForecast(1644103534, 1644067594, 1644103534, tempForecast[4], 87, 30.28F),
        DateForecast(1644190054, 1644153934, 1644190054, tempForecast[5], 92, 29.8F),
        DateForecast(1644276514, 1644240214, 1644276514, tempForecast[6], 90, 30.06F),
        DateForecast(1644363034, 1644326554, 1644363034, tempForecast[7], 84, 29.64F),
        DateForecast(1644449494, 1644412894, 1644449494, tempForecast[8], 86, 27.98F),
        DateForecast(1644535954, 1644499174, 1644535954, tempForecast[9], 81, 28.06F),
        DateForecast(1644585514, 1644671794, 1644585514, tempForecast[10], 80, 30.56F),
        DateForecast(1644671794, 1644671794, 1644709054, tempForecast[11], 77, 30.33F),
        DateForecast(1644758134, 1644758134, 1644795454, tempForecast[12], 83, 30.27F),
        DateForecast(1644844414, 1644844414, 1644882034, tempForecast[13], 80, 30.36F),
        DateForecast(1644930754, 1644930754, 1644968434, tempForecast[14], 80, 29.21F),
        DateForecast(1645017034, 1645017034, 1645054894, tempForecast[15], 82, 29.97F)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "Forecast"
        val backgroundColor = ColorDrawable(Color.parseColor("#FFA500"))
        actionBar?.setBackgroundDrawable(backgroundColor)


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = MyAdapter(adapterData)
    }

}