package com.example.weatherapp.userInterface.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

    @Inject lateinit var forecastViewModel: ForecastViewModel
    private lateinit var binding: ForecastFragmentBinding
    private val args: ForecastFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ForecastFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ForecastFragmentBinding.bind(view)
        requireActivity().title = "Forecast"
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onResume() {
        super.onResume()
        forecastViewModel.forecast.observe(this) {
                forecast -> bindData(forecast)
        }
        try {
            if (args.zipCode != null) {
                forecastViewModel.loadData(args.zipCode)
            } else {
                forecastViewModel.loadData(args.latitude, args.longitude)
            }
        } catch (exception: HttpException) {
            if (exception.code() == 404) {
                ErrorDialogFragment().show(childFragmentManager, ErrorDialogFragment.TAG)
            }
        }
    }

    private fun bindData(foreCast: Forecast) {
        binding.recyclerView.adapter = MyAdapter(foreCast.list)
    }

}
