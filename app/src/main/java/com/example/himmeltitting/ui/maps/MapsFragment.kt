package com.example.himmeltitting.ui.maps

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
import com.example.himmeltitting.ui.SharedViewModel
import com.example.himmeltitting.R
import com.example.himmeltitting.databinding.FragmentMapsBinding
import com.example.himmeltitting.utils.currentDate
import com.example.himmeltitting.utils.getChosenTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import java.io.IOException

/**
 * Fragment containing GoogleMap.
 */
class MapsFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var binding: FragmentMapsBinding
    private val viewModel: SharedViewModel by activityViewModels()

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var currentLatLng: LatLng
    private var marker: Marker? = null
    private var lastLatLng: LatLng? = null
    private lateinit var fusedLocationClient : FusedLocationProviderClient

    companion object{
        const val LOCATION_REQUEST_CODE = 1
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container:  ViewGroup?,
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
        binding.calendarButton.text = currentDate()
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
     *
     * Used on initial opening of the app or during switching between fragments.
     *
     */
    private fun setDefaultMapLocationNorway(googleMap: GoogleMap) = if (lastLatLng == null) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(62.47, 8.46), 4f))
    } else {
        lastLatLng?.let { placeMarkerOnMap(it) }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng!!, 12f))
    }

    /**
     * Initializes the map theme. The function is called in onMapReady().
     * Retro style is the default map theme for light mode,
     * Night style is the default theme for dark mode.
     * But the theme also changes depending on the switch which was clicked in settings.
     */
    private fun setMapTheme(googleMap: GoogleMap){
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
     * Checks location access permissions from user. Finds longitude and latitude on the current location.
     */
    private fun setUpMap() {
        // permission checks
        if (ActivityCompat.checkSelfPermission(this.requireContext(),  Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this.requireActivity()) { location ->
            if(location != null){
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
        // change marker style
        //move to separate method?
        val mBitmap = context?.let { getBitmapFromVectorDrawable(it, R.drawable.ic_location) }
        markerOptions.icon(mBitmap?.let { BitmapDescriptorFactory.fromBitmap(it) })
        marker = mMap.addMarker(markerOptions)
        marker?.showInfoWindow()
        updateLatLng()
    }

    /**
     *
     */
    override fun onMarkerClick(p0: Marker)= false

    /**
     * Initialized on OnClickListener for map.
     */
    private fun addOnMapClickListener(){
        mMap.setOnMapClickListener { latlng ->
            val location = LatLng(latlng.latitude, latlng.longitude)
            currentLatLng = location
            lastLatLng = currentLatLng
            placeMarkerOnMap(location)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
        }

    }

    /**
     * Initialize SearchView with text. Text input from user is used to get
     * coordinates and place marker on the map.
     */
    private fun addSearchView(){
        val searchView = binding.idSearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val location: String = searchView.query.toString()
                // list of address where results will be stored
                var addressList: List<Address>? = null
                //  initialize a geo coder.
                val geocoder = Geocoder(activity)
                try {
                    // get location from the location name, add to address list.
                    addressList = geocoder.getFromLocationName(location, 1)
                } catch (e: IOException) {
                    e.printStackTrace() }

                // get the first location in the list
                if (addressList != null) {
                    if(addressList.isNotEmpty()){
                        val address: Address = addressList[0]
                        val latLng = LatLng(address.latitude, address.longitude)
                        currentLatLng = latLng
                        lastLatLng = currentLatLng
                        placeMarkerOnMap(latLng)
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                        return false }
                }
                setToast("Fant ikke noe med dette navnet")
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
     * Generates and displays a Toast message with custom input.
     */
    private fun setToast(message: String){
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }


}