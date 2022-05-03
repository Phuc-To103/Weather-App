package com.example.weatherapp.userInterface.forecast

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.userInterface.dialog.ErrorDialogFragment
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ForecastFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.forecast_fragment) {
    private lateinit var binding: ForecastFragmentBinding
    @Inject lateinit var forecastViewModel: ForecastViewModel
    private val args by navArgs<ForecastFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ForecastFragmentBinding.bind(view)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onResume() {
        super.onResume()
        forecastViewModel.forecast.observe(this) {
                forecast -> bindData(forecast)
        }
        try {
            forecastViewModel.loadData(args.zipCode)
        } catch (exception: HttpException) {
            if (exception.code() == 404) {
                ErrorDialogFragment().show(childFragmentManager, ErrorDialogFragment.TAG)
            }
        }
        if(args.zipCode.length == 5 && args.zipCode.all { it.isDigit() }) {
            forecastViewModel.loadData(args.zipCode)
        } else {
            forecastViewModel.loadData(args.latitude, args.longitude)
        }

    }


    private fun bindData(forecast: Forecast) {
        val adapter = MyAdapter(forecast.list)
        binding.recyclerView.adapter = adapter
        adapter.setOnDayClickListener(object : MyAdapter.OnDayListener{
            override fun onDayClick(index: Int) {
                val action = ForecastFragmentDirections.actionForecastFragmentToForecastDetailsFragment(
                    forecastViewModel.forecast.value!!.list[index])
                findNavController().navigate(action)
            }

        })
    }

}
