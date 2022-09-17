package com.ekochkov.santehnikatestapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ekochkov.santehnikatestapp.databinding.FragmentHomeBinding
import com.ekochkov.santehnikatestapp.utils.Constants
import com.ekochkov.santehnikatestapp.viewModel.HomeFragmentViewModel

class HomeFragment: Fragment() {

    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.pointAddressLiveData.observe(viewLifecycleOwner) {
            binding.address.text = it
        }

        binding.toMapBtn.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_map)
        }
    }

    override fun onResume() {
        super.onResume()
        val result = findNavController().currentBackStackEntry?.savedStateHandle?.get<String>(Constants.KEY_GEOCODE)
        if (!result.isNullOrEmpty() && result!=Constants.EMPTY_GEOCODE) {
            viewModel.getAddress(result)
        }
    }
}