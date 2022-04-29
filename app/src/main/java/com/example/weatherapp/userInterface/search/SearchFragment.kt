package com.example.weatherapp.userInterface.search

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.databinding.SearchFragmentBinding
import com.example.weatherapp.service.NotificationService
import com.example.weatherapp.userInterface.dialog.ErrorDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val CHANNEL_ID = "Notification Channel"
@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment) {

    @Inject
    lateinit var searchViewModel: SearchViewModel
    private lateinit var binding: SearchFragmentBinding
    private var notificationActive: Boolean = false
    private lateinit var serviceIntent: Intent

    private var latitude: String? = null
    private var longitude: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Search"
        createNotificationChannel()

        serviceIntent = Intent(requireActivity().applicationContext, NotificationService::class.java)
        requireActivity().registerReceiver(updateNotification, IntentFilter(NotificationService.TIMER_UPDATED))

        searchViewModel.enableButton.observe(viewLifecycleOwner) { enable ->
            binding.searchButton.isEnabled = enable
        }

        searchViewModel.showErrorDialog.observe(viewLifecycleOwner) { showError ->
            if (showError) {
                ErrorDialogFragment().show(childFragmentManager, ErrorDialogFragment.TAG)
            }
        }

        binding.zipCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.toString()?.let { searchViewModel.updateZipCode(it) }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        binding.searchButton.setOnClickListener {
            searchViewModel.searchButtonClicked()
            if(!(searchViewModel.showErrorDialog.value!!)) {
                val action = SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(
                    searchViewModel.currentConditions.value,
                    searchViewModel.getZipCode(),
                    null,
                    null
                )
                Navigation.findNavController(it).navigate(action)
            } else {
                searchViewModel.resetErrorDialog()
            }
        }
        binding.locationButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission((activity as MainActivity), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                sendLocationData()
            } else {
                (activity as MainActivity).requestLocationPermission()
            }
        }
        binding.notificationButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission((activity as MainActivity), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLocation()
                if (!notificationActive) {
                    notificationActive = true
                    serviceIntent.putExtra(NotificationService.ELAPSED_TIME, 0)
                    requireActivity().startService(serviceIntent)
                    sendNotification()
                    binding.notificationButton.text = getString(R.string.disable_notifications)
                } else {
                    notificationActive = false
                    requireActivity().stopService(serviceIntent)
                    with(NotificationManagerCompat.from(requireContext())) {
                        cancel(1)
                    }
                    binding.notificationButton.text = getString(R.string.enable_notifications)
                }
            } else {
                (activity as MainActivity).requestLocationPermission()
            }
        }

    }

    private fun getLocation() {
        latitude = (activity as MainActivity).getLatitude()
        longitude = (activity as MainActivity).getLongitude()
        try {
            searchViewModel.notificationButtonClicked(latitude!!, longitude!!)
        } catch (e : NullPointerException) {
            latitude = "37.434"
            longitude = "-122.083"
            searchViewModel.notificationButtonClicked(latitude!!, longitude!!)
        }
    }
    private fun sendLocationData() {
        latitude = (activity as MainActivity).getLatitude()
        longitude = (activity as MainActivity).getLongitude()
        try {
            searchViewModel.locationButtonClicked(latitude!!, longitude!!)
        } catch (e : NullPointerException) {
            latitude = "37.434"
            longitude = "-122.083"
            searchViewModel.locationButtonClicked(latitude!!, longitude!!)
        }
        if(!(searchViewModel.showErrorDialog.value!!)) {
            val currentConditionsArg = SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(
                searchViewModel.currentConditions.value,
                null,
                latitude,
                longitude
            )
            Navigation.findNavController(binding.root).navigate(currentConditionsArg)
        } else {
            searchViewModel.resetErrorDialog()
        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Channel Title"
            val descriptionText = "Notification Channel Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.sun)
            .setContentTitle("Notification Title")
            .setContentText("Notification Text")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(requireContext())) {
            notify(1, builder.build())
        }
    }
    private val updateNotification: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            getLocation()
            val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.notify_location,
                    searchViewModel.currentConditions.value?.name))
                .setContentText(getString(R.string.notify_temp,
                    searchViewModel.currentConditions.value?.main?.temp?.toInt()))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(requireContext())) {
                notify(1, builder.build())
            }
        }
    }
}