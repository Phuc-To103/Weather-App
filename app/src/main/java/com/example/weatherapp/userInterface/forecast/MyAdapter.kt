package com.example.weatherapp.userInterface.forecast

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.model.DateForecast
import com.example.weatherapp.R
import com.example.weatherapp.databinding.RowDataBinding
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@SuppressLint("NewApi")
class MyAdapter(private val data: List<DateForecast>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = RowDataBinding.bind(view)
        private val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
        private val hourTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

        @SuppressLint("NewApi")
        fun bind(data: DateForecast) {
            val getDateTime =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(data.dt), ZoneId.systemDefault())
            val getHourTimeSR =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(data.sunrise), ZoneId.systemDefault())
            val getHourTimeSS =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(data.sunset), ZoneId.systemDefault())
            binding.dateView.text = dateFormatter.format(getDateTime)
            binding.sunriseView.append(hourTimeFormatter.format(getHourTimeSR))
            binding.sunsetView.append(hourTimeFormatter.format(getHourTimeSS))
            binding.tempView.append(data.temp.day.toInt().toString() + "°F")
            binding.highView.append(data.temp.max.toInt().toString() + "°F")
            binding.lowView.append(data.temp.min.toInt().toString() + "°F")

            val iconName = data.weather.firstOrNull()?.icon
            val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
            Glide.with(binding.iconView)
                .load(iconUrl)
                .into(binding.iconView)
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