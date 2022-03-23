package com.example.weatherapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weatherapp.databinding.SearchFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment) {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var binding: SearchFragmentBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = "Search"
        binding = SearchFragmentBinding.bind(view)
        searchViewModel = SearchViewModel()
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
    }
}