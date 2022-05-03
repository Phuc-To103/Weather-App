package com.example.weatherapp.userInterface.search

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.databinding.SearchFragmentBinding
import com.example.weatherapp.service.NotificationService
import com.example.weatherapp.util.Constants
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment) {
    private lateinit var searchViewModel: SearchViewModel
    lateinit var binding: SearchFragmentBinding

    private lateinit var locationRequest: LocationRequest
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var latitude: Float? = null
    private var longitude: Float? = null
    private var notificationIsActive: Boolean = false
    private lateinit var serviceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.MINUTES.toMillis(30)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(1)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.locations.forEach{
                    Log.d(ContentValues.TAG,"JOHN 1/Callback Latitude: " + it.latitude.toString() + ", Longitude: " + it.longitude.toString())
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Search"
        binding = SearchFragmentBinding.bind(view)
        searchViewModel = SearchViewModel()
        createNotificationChannel()
        serviceIntent = Intent(requireActivity().applicationContext, NotificationService::class.java)
    }

    override fun onResume() {
        super.onResume()

        binding.zipCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.toString()?.let { searchViewModel.updateZipCode(it) }
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        searchViewModel.enableButton.observe(this) { enable ->
            binding.searchButton.isEnabled = enable
        }

        binding.searchButton.setOnClickListener {
            val action = SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment(
                searchViewModel.getZipCode()
            )
            findNavController().navigate(action)
        }

        binding.locationButton.setOnClickListener{
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Constants.manifestLocationPermission
                ) != PackageManager.PERMISSION_GRANTED){
                requestPermission()
            } else {
                requestLocation()
            }
        }
        binding.notificationButton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(requireContext(),
                    Constants.manifestLocationPermission
                ) != PackageManager.PERMISSION_GRANTED){
                requestPermission()
            } else {
                requestLocation()
            }
        }
    }


    private fun requestPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                Constants.manifestLocationPermission
            )) {
            AlertDialog.Builder(requireContext())
                .setTitle("Permission Needed")
                .setMessage("We need permission for your location to provide accurate weather data")
                .setPositiveButton("Ok"){ dialog: DialogInterface, which: Int ->
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Constants.manifestLocationPermission),
                        Constants.REQUEST_CODE_COARSE_LOCATION
                    )
                }
                .setNegativeButton("Cancel"){ dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }
                .create().show()

        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Constants.manifestLocationPermission),
                Constants.REQUEST_CODE_COARSE_LOCATION
            )
        }
        runBlocking {
            delay(3000)
            requestLocation()
        }
    }



    @SuppressLint("MissingPermission")
    private fun requestLocation(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            Log.d(ContentValues.TAG,"JOHN 1 Latitude: " + location.latitude.toString() + ", Longitude: " + location.longitude.toString())
            latitude = location.latitude.toFloat()
            longitude = location.longitude.toFloat()
            Log.d(ContentValues.TAG,"John 3 Latitude: " + latitude + ", Longitude: " + longitude)
            notificationCondition()
            if(latitude != null && longitude != null) {
                val action = SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment("", latitude!!, longitude!!)
                findNavController().navigate(action)
            }
        }.addOnFailureListener {
            Log.d(ContentValues.TAG, "Failed getting current location")
        }
    }

    private fun notificationCondition(){
        if (!notificationIsActive){
            notificationIsActive = true
            serviceIntent.putExtra(Constants.ELAPSED_TIME, 0)
            requireActivity().startService(serviceIntent)
            binding.notificationButton.text = getString(R.string.turn_notification_off)
        } else {
            notificationIsActive = false
            requireActivity().stopService(serviceIntent)
            with(NotificationManagerCompat.from(requireContext())) {
                cancel(1)
            }
            binding.notificationButton.text = getString(R.string.turn_notification_on)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(Constants.NOTIFICATION_CHANNEL_ID, Constants.NOTIFICATION_CHANNEL_NAME, importance).apply {
                description = "Weather Channel"
            }
            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}