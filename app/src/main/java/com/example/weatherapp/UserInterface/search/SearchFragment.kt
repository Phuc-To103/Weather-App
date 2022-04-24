package com.example.weatherapp.UserInterface.search

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R
import com.example.weatherapp.UserInterface.dialog.ErrorDialogFragment.Companion.TAG
import com.example.weatherapp.databinding.SearchFragmentBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment) {
    private lateinit var searchViewModel: SearchViewModel
    lateinit var binding: SearchFragmentBinding

    private val manifestRequestCoarseLocation: String = Manifest.permission.ACCESS_COARSE_LOCATION
    private val manifestPermissionCode: Int = 1234

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var latitude: Float? = null
    private var longitude: Float? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Search"
        binding = SearchFragmentBinding.bind(view)
        searchViewModel = SearchViewModel()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                val lastLocationResult = p0.lastLocation
                Log.d(TAG,"JOHN-1 Latitude: " + lastLocationResult.latitude.toString() + ", Longitude: " + lastLocationResult.longitude.toString())
                latitude = lastLocationResult.latitude.toFloat()
                longitude = lastLocationResult.longitude.toFloat()
                Log.d(TAG,"JOHN-2 Latitude: " + latitude + ", Longitude: " + longitude)
                super.onLocationResult(p0)
            }
        }
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
            if (checkForPermission()){
                Log.d(TAG,"JOHN-2.0 Latitude: " + latitude + ", Longitude: " + longitude)
                if(latitude != null && longitude != null) {
                    val action = SearchFragmentDirections.actionSearchFragmentToCurrentConditionsFragment("", latitude!!, longitude!!)
                    findNavController().navigate(action)
                }
                Toast.makeText(requireContext(),"Permission Already Granted", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun checkForPermission() : Boolean {
        val checkSelfPermission: Boolean = ActivityCompat.checkSelfPermission(requireContext(), manifestRequestCoarseLocation) == PackageManager.PERMISSION_GRANTED
        if(checkSelfPermission){
            requestLocation()
        } else {
            requestPermission()
        }
        return checkSelfPermission
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),manifestRequestCoarseLocation)) {
            AlertDialog.Builder(requireContext())
                .setTitle("Permission Needed")
                .setMessage("we need permission for fine location")
                .setPositiveButton("Ok") { dialog: DialogInterface, which: Int ->
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(
                        manifestRequestCoarseLocation
                    ), manifestPermissionCode
                    )
                }
                .setNegativeButton("Cancel"){ dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }
                .create()
                .show()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(
                manifestRequestCoarseLocation
            ), manifestPermissionCode
            )
        }
    }



    @SuppressLint("MissingPermission")
    private fun requestLocation(){
        val locationRequest = com.google.android.gms.location.LocationRequest.create()
        locationRequest.priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

}