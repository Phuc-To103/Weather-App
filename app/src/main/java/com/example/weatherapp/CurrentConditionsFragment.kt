package com.example.weatherapp

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weatherapp.databinding.CurrentConditionFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class CurrentConditionsFragment : Fragment(R.layout.current_condition_fragment) {

    private lateinit var binding: CurrentConditionFragmentBinding
    @Inject
    lateinit var currentConditionsViewModel: CurrentConditionsViewModel
    private val args by navArgs<CurrentConditionsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CurrentConditionFragmentBinding.bind(view)

        binding.button.setOnClickListener {
            val action =
                CurrentConditionsFragmentDirections.actionCurrentConditionsFragmentToForeCastFragment(
                    args.zipCode
                )
            findNavController().navigate(action)

        }
    }

    override fun onResume() {
        super.onResume()
        currentConditionsViewModel.currentConditions.observe(this) { currentConditions ->
            bindData(currentConditions)
        }
        try {
            currentConditionsViewModel.loadData(args.zipCode)
        } catch (exception: HttpException) {
            if (exception.code() == 404) {
                ErrorDialogFragment().show(childFragmentManager, ErrorDialogFragment.TAG)
            }
        }
    }

    private fun bindData(currentConditions: CurrentConditions) {
        binding.cityName.text = currentConditions.name
        binding.temperature.text =
            getString(R.string.temperature, currentConditions.main.temp.toInt())
        binding.feelsLike.text =
            getString(R.string.feel_like, currentConditions.main.feelsLike.toInt())
        binding.lowTemp.text =
            getString(R.string.current_low, currentConditions.main.tempMin.toInt())
        binding.highTemp.text =
            getString(R.string.current_high, currentConditions.main.tempMax.toInt())
        binding.huminityTemp.text =
            getString(R.string.current_humidity, currentConditions.main.humidity.toInt())
        binding.pressureTemp.text =
            getString(R.string.current_pressure, currentConditions.main.pressure.toInt())

        val iconName = currentConditions.weather.firstOrNull()?.icon
        val iconUrl = "https://openweathermap.org/img/wn/${iconName}@2x.png"
        Glide.with(this)
            .load(iconUrl)
            .into(binding.iconTemp)

    }
}