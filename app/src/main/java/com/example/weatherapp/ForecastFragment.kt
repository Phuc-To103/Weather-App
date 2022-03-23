package com.example.weatherapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.databinding.ForecastFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.forecast_fragment) {
    private lateinit var binding: ForecastFragmentBinding
    @Inject lateinit var viewModel: ForecastViewModel
    private val args by navArgs<ForecastFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ForecastFragmentBinding.bind(view)
        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
    }

    override fun onResume() {
        super.onResume()
        viewModel.forecast.observe(this) {
                forecast -> bindData(forecast)
        }
        try {
            viewModel.loadData(args.zipCode)
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
