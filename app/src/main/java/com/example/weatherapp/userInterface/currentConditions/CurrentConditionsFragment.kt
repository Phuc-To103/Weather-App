package com.example.weatherapp.userInterface.currentConditions


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weatherapp.model.CurrentConditions
import com.example.weatherapp.R
import com.example.weatherapp.databinding.CurrentConditionFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CurrentConditionsFragment : Fragment(R.layout.current_condition_fragment) {

    private val args: CurrentConditionsFragmentArgs by navArgs()
    private lateinit var binding: CurrentConditionFragmentBinding
    private lateinit var currentConditionsViewModel: CurrentConditionsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CurrentConditionFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Current Conditions"
        currentConditionsViewModel = CurrentConditionsViewModel()
        binding.forecastButton.setOnClickListener {
            if (args.zipCode != null) {
                val zipCodeAction =
                    CurrentConditionsFragmentDirections.actionCurrentConditionsFragmentToForeCastFragment(
                        args.zipCode,
                        null,
                        null
                    )
                Navigation.findNavController(it).navigate(zipCodeAction)
            } else {
                val latLonAction =
                    CurrentConditionsFragmentDirections.actionCurrentConditionsFragmentToForeCastFragment(
                        null,
                        args.latitude,
                        args.longitude
                    )
                Navigation.findNavController(it).navigate(latLonAction)
            }
        }
    }
        override fun onResume() {
            super.onResume()
            currentConditionsViewModel.currentConditions.observe(this) { currentConditions ->
                bindData(currentConditions)
            }
            currentConditionsViewModel.loadData(args.currentConditionsArg!!)
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
