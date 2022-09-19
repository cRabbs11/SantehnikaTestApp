package com.ekochkov.santehnikatestapp.view.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ekochkov.santehnikatestapp.R
import com.ekochkov.santehnikatestapp.databinding.FragmentMapBinding
import com.ekochkov.santehnikatestapp.utils.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider

const val MAP_KIT_ZOOM_11 = 11.0f
const val MAP_KIT_AZIMUTH_0 = 0.0f
const val MAP_KIT_TILT_0 = 0.0f
const val MAP_KIT_ANIMATION_DURATION_1 = 1f


class MapFragment: Fragment() {

    private lateinit var binding : FragmentMapBinding
    private var geocode = Constants.EMPTY_GEOCODE
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var placemarkMapObject : PlacemarkMapObject? = null

    private val permissionLocationLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                if (location!=null) {
                    moveToCoords(location.latitude, location.longitude)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toHomeBtn.setOnClickListener {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(Constants.KEY_GEOCODE, geocode)
            findNavController().navigateUp()
        }

        if (checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                if (location!=null) {
                    moveToCoords(location.latitude, location.longitude)
                }
            }
        } else {
            requestPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        initMap()
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    private fun initMap() {
        MapKitFactory.initialize(requireContext())
        binding.mapView.map.addInputListener(listener)
    }

    private fun moveToCoords(lat: Double, lon: Double) {
        binding.mapView.map.move(
            CameraPosition(Point(lat, lon), MAP_KIT_ZOOM_11, MAP_KIT_AZIMUTH_0, MAP_KIT_TILT_0),
            Animation(Animation.Type.SMOOTH, MAP_KIT_ANIMATION_DURATION_1),
            null
        )
    }

    private val listener = object: InputListener {
        override fun onMapTap(p0: Map, p1: Point) {
            geocode = "${p1.latitude}, ${p1.longitude}"
            placemarkMapObject?.let {
                binding.mapView.map.mapObjects.remove(it)
            }
            placemarkMapObject = binding.mapView.map.mapObjects.addPlacemark(p1, ImageProvider.fromResource(requireContext(), R.drawable.map_pin))
        }

        override fun onMapLongTap(p0: Map, p1: Point) {

        }
    }

    private fun requestPermission(permission: String) {
        when (permission) {
            Manifest.permission.ACCESS_COARSE_LOCATION -> {
                permissionLocationLauncher.launch(permission)
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission) == PackageManager.PERMISSION_GRANTED
    }
}