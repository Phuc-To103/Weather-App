package com.example.weatherapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@SuppressLint("NewApi")
class MyAdapter(private val data: List<DateForecast>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var dateView: TextView = view.findViewById(R.id.date)
        private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
        private val hourTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        private var tempView: TextView = view.findViewById(R.id.temp)
        private var highView: TextView = view.findViewById(R.id.high)
        private var lowView: TextView = view.findViewById(R.id.low)
        private var sunRiseView: TextView = view.findViewById(R.id.sunrise)
        private var sunSetView: TextView = view.findViewById(R.id.sunset)
        private var iconView: ImageView = view.findViewById(R.id.recyclerView_icon)

        @SuppressLint("NewApi")
        fun bind(data: DateForecast) {
            val getDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(data.dt), ZoneId.systemDefault())
            val getHourTimeSR =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(data.sunrise), ZoneId.systemDefault())
            val getHourTimeSS =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(data.sunset), ZoneId.systemDefault())
            dateView.text = dateFormatter.format(getDateTime)
            sunRiseView.append(hourTimeFormatter.format(getHourTimeSR))
            sunSetView.append(hourTimeFormatter.format(getHourTimeSS))
            tempView.append(data.temp.day.toInt().toString() + "°F")
            highView.append(data.temp.max.toInt().toString() + "°F")
            lowView.append(data.temp.min.toInt().toString() + "°F")

            val iconName = data.weather.firstOrNull()?.icon
            val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
            Glide.with(iconView)
                .load(iconUrl)
                .into(iconView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_data, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

}