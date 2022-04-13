package com.example.weatherapp.UserInterface.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.R

class ErrorDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage("Error fetching data for that zip code")
            .setPositiveButton(R.string.ok) { DialogInterface, Int ->
                findNavController().navigate(R.id.searchFragment)
            }
            .create()

    companion object{
        const val TAG = "ErrorDialogFragment"
    }

}