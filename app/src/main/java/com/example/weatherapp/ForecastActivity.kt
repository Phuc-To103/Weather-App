package com.example.weatherapp

import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.ActivityForecastBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ForecastActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForecastBinding

    @Inject
    lateinit var viewModel: ForecastViewModel

    private lateinit var myConstraintLayout: ConstraintLayout
    private lateinit var animationDrawable: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForecastBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.title = "Forecast"
        val backgroundColor = ColorDrawable(Color.parseColor("#ffb347"))
        actionBar?.setBackgroundDrawable(backgroundColor)

        myConstraintLayout = findViewById(R.id.ConstraintLayout_forecast)
        animationDrawable = myConstraintLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(15)
        animationDrawable.setExitFadeDuration(3000)
        animationDrawable.start()

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.forecast.observe(this) {
                forecast -> bindData(forecast)
        }
        viewModel.loadData()
    }


    private fun bindData(foreCast: Forecast) {
        binding.recyclerView.adapter = MyAdapter(foreCast.list)
    }

}