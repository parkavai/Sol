package com.example.soleklart.ui.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.soleklart.R
import com.example.soleklart.databinding.FragmentMapsBinding
import com.example.soleklart.ui.SharedViewModel
import com.example.soleklart.utils.currentDate
import com.example.soleklart.utils.getChosenTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.io.IOException

/**
 * Fragment containing a map which is implemented using Google Maps SDK. This fragment has functionality to
 * display locations, place markers, execute location search, respond to a click and show current location.
 * Fused Location Provider API from Google Play services is used  to provide the current device location.
 * Location search from input text is implemented using Geocoder API.
 */

class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var binding: FragmentMapsBinding
    private val viewModel: SharedViewModel by activityViewModels()
    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var currentLatLng: LatLng
    private var marker: Marker? = null
    private var lastLatLng: LatLng? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        const val LOCATION_REQUEST_CODE = 1
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     *  Makes sure that the view is initialized and gets fused location provider.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mapView = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapView.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(view.context)
        setUpCalendar()
    }

    /**
     * Initializes map listeners, default location, search view and map style.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setDefaultMapLocationNorway(mMap)
        setMapTheme(mMap)
        setUpMap()
        addOnMapClickListener()
        addSearchView()
        mMap.setPadding(0, 0, 0, 70)
    }

    /**
     * Function that either sets the initialial location as Norway is no previous location choice exists
     * or puts the marker at the last visited place.
     * Used on initial opening of the app or during switching between fragments.
     */
    private fun setDefaultMapLocationNorway(googleMap: GoogleMap) = if (lastLatLng == null) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(62.47, 8.46), 4f))
    } else {
        lastLatLng?.let { placeMarkerOnMap(it) }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng!!, 12f))
    }


    /**
     * Adds observer to date to update text in calendar button,
     * sets text as current date on start and
     * sets onclick listener for calendar button
     */
    private fun setUpCalendar() {
        viewModel.date.observe(viewLifecycleOwner) {
            binding.calendarButton.text = it
        }
        binding.calendarButton.text = currentDate()
        binding.calendarButton.setOnClickListener {
            if (binding.calendarFragment.visibility == View.GONE) {
                setCalendarVisibility(true)
            } else {
                setCalendarVisibility(false)
            }
        }
    }

    /**
     * Checks location access permissions from user. Finds longitude and latitude on the current location
     * and adjust display to show the current location.
     */
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this.requireActivity()) { location ->
            if (location != null) {
                lastLocation = location
                currentLatLng = LatLng(location.latitude, location.longitude)
                lastLatLng = currentLatLng
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }

    /**
     * Initialized marker style and places marker at the given position.
     */
    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        marker?.remove()
        val markerOptions = MarkerOptions().position(currentLatLong)
        val mBitmap = context?.let { getBitmapFromVectorDrawable(it, R.drawable.ic_location) }
        markerOptions.icon(mBitmap?.let { BitmapDescriptorFactory.fromBitmap(it) })
        marker = mMap.addMarker(markerOptions)
        marker?.showInfoWindow()
        updateLatLng()
    }



    /**
     * Initialized on OnClickListener for map.
     */
    private fun addOnMapClickListener() {
        mMap.setOnMapClickListener { latlng ->
            val location = LatLng(latlng.latitude, latlng.longitude)
            currentLatLng = location
            lastLatLng = currentLatLng
            placeMarkerOnMap(location)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
            binding.idSearchView.setQuery("", false)
            binding.idSearchView.clearFocus()
        }
    }

    /**
     * Initialize functionality to perform location with text. Text input from user is used to get
     * coordinates and place marker on the map. Method return false if no location has been found.
     */
    private fun addSearchView() {
        val searchView = binding.idSearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val location: String = searchView.query.toString()
                // list of address where results will be stored
                var addressList: List<Address>? = null
                val geocoder = Geocoder(activity)
                try {
                    addressList = geocoder.getFromLocationName(location, 1)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                // always get the first location in the list
                if (addressList != null) {
                    if (addressList.isNotEmpty()) {
                        val address: Address = addressList[0]
                        val latLng = LatLng(address.latitude, address.longitude)
                        currentLatLng = latLng
                        lastLatLng = currentLatLng
                        placeMarkerOnMap(latLng)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                        return false
                    }
                }
                setToast(getString(R.string.Error_placeNotFound))
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    /**
     * Updates latlong coordinates in sharedviewmodel
     */
    private fun updateLatLng() {
        viewModel.setLatLng(currentLatLng)
    }

    /**
     * Initializes the map theme. The function is called in onMapReady().
     * Retro style is the default map theme for light mode,
     * Night style is the default theme for dark mode.
     * But the theme also changes depending on the switch which was clicked in settings.
     */
    private fun setMapTheme(googleMap: GoogleMap) {
        var theme = 0
        when (resources.getString(R.string.mode)) {
            "Night" -> theme = R.raw.night_style
            "Day" -> theme = getChosenTheme()
        }
        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
            requireActivity(),
            theme
        )
        googleMap.setMapStyle(mapStyleOptions)
    }

    /**
     * Converts vector icon into a bitmap. Used when initializing marker style.
     */
    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    /**
     * Generates and displays a Toast message with custom message input.
     * Primary use is signaling to the user that a wrong input has been given.
     */
    private fun setToast(message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }


    /**
     * Shows calendar fragment
     */
    fun setCalendarVisibility(isVisible: Boolean) {
        if (isVisible) {
            binding.calendarFragment.visibility = View.VISIBLE

        } else {
            binding.calendarFragment.visibility = View.GONE
        }
    }

    /**
     * Initialize and override method that responds to click on the marker.
     */
    override fun onMarkerClick(p0: Marker) = false



}