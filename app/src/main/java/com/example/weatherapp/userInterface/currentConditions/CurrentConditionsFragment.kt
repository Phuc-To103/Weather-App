package com.example.weatherapp.userInterface.currentConditions


import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.weatherapp.MainActivity
import com.example.weatherapp.userInterface.dialog.ErrorDialogFragment
import com.example.weatherapp.model.CurrentConditions
import com.example.weatherapp.R
import com.example.weatherapp.userInterface.dialog.ErrorDialogFragment.Companion.TAG
import com.example.weatherapp.databinding.CurrentConditionFragmentBinding
import com.example.weatherapp.service.NotificationService
import com.example.weatherapp.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import javax.inject.Inject

@AndroidEntryPoint
class CurrentConditionsFragment : Fragment(R.layout.current_condition_fragment) {

    private lateinit var binding: CurrentConditionFragmentBinding
    @Inject
    lateinit var currentConditionsViewModel: CurrentConditionsViewModel
    private val args by navArgs<CurrentConditionsFragmentArgs>()
    private lateinit var weatherService: NotificationService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CurrentConditionFragmentBinding.bind(view)
        weatherService = NotificationService()
        binding.button.setOnClickListener {
            val action =
                CurrentConditionsFragmentDirections.actionCurrentConditionsFragmentToForeCastFragment(
                    args.zipCode,args.latitude,args.longitude
                )
            findNavController().navigate(action)
        }
        requireActivity().registerReceiver(updateNotification, IntentFilter(Constants.TIMER_UPDATED))
    }

    override fun onResume() {
        super.onResume()
        currentConditionsViewModel.currentConditions.observe(this) { currentConditions ->
            bindData(currentConditions)
        }
        Log.d(TAG, "JOHN-3 Latitude: " + args.latitude + ", Longitude: " + args.longitude)
        try {
            if(args.zipCode.length == 5 && args.zipCode.all { it.isDigit() }) {
                currentConditionsViewModel.loadData(args.zipCode)
            }else{
                currentConditionsViewModel.loadData(args.latitude, args.longitude)
                sendNotification()
            }
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

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(requireContext(),
            Constants.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.sun)
            .setContentTitle(getString(R.string.notify_location,
                currentConditionsViewModel.currentConditions.value?.name))
            .setContentText(getString(R.string.notify_currentTemp,
                currentConditionsViewModel.currentConditions.value?.main?.temp?.toInt()))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(requireContext())) {
            notify(1, builder.build())
        }
    }

    private val updateNotification: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val builder = NotificationCompat.Builder(requireContext(),
                Constants.NOTIFICATION_CHANNEL_ID
            )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.notify_location,
                    currentConditionsViewModel.currentConditions.value?.name))
                .setContentText(getString(R.string.notify_currentTemp,
                    currentConditionsViewModel.currentConditions.value?.main?.temp?.toInt()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getMainActivityPendingIntent())
            with(NotificationManagerCompat.from(requireContext())) {
                notify(1, builder.build())
            }
        }
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        requireContext(), 0, Intent(requireContext(), MainActivity::class.java).also {
            it.action = Constants.ACTION_SHOW_CURRENT_CONDITIONS_FRAGMENT
        }, PendingIntent.FLAG_UPDATE_CURRENT
    )
}