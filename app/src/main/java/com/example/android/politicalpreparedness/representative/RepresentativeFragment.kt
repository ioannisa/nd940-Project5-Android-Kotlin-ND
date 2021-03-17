package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.model.Representative
import com.google.android.gms.location.*
import java.util.*


class RepresentativeFragment : Fragment() {

    lateinit var states: Array<out String>

    companion object {
        //TODO COMPLETED: Add Constant for Location request
        private const val REQUEST_LOCATION_PERMISSION = 599
    }

    //TODO COMPLETED: Declare ViewModel
    private lateinit var viewModel: RepresentativeViewModel

    private lateinit var binding: FragmentRepresentativeBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        //TODO COMPLETED: Establish bindings
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        // make a copy of the array of the 50 States of the United States of America
        states = resources.getStringArray(com.example.android.politicalpreparedness.R.array.states)

        //TODO COMPLETED: Add ViewModel values and create ViewModel
        val viewModelFactory = RepresentativeViewModelFractory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RepresentativeViewModel::class.java)
        binding.viewModel = viewModel

        //TODO: Define and assign Representative adapter
        //TODO: Populate Representative adapter
        val adapterRepresentatives = RepresentativeListAdapter()
        binding.rvRepresentatives.adapter = adapterRepresentatives

        //TODO COMPLETED: Establish button listeners for field and location search
        binding.buttonSearch.setOnClickListener {
            // on button click hide keyboard
            hideKeyboard()

            // create Address object from the filled form
            val line2 = binding.addressLine2.text.toString()
            val address = Address(
                    line1 = binding.addressLine1.text.toString(),
                    line2 = if (line2 == "") null; else line2,
                    city = binding.city.text.toString(),
                    state = binding.state.selectedItem.toString(),
                    zip = binding.zip.text.toString()
            )

            // retrieve representative based on the created Address object
            viewModel.getRepresentatives(address)
        }

        // when pressing the "location" button...
        binding.buttonLocation.setOnClickListener {
            // hide the keyboard
            hideKeyboard()
            // retrieve data based on permissions aproval
            checkLocationPermissions()
        }

        return binding.root
    }

    /**
     * Upon permission request, if the user result to that request was positive
     * then get the location candidates based on the device location
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO COMPLETED: Handle location permission result to get location on permission granted
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }
        }
    }

    /**
     * If permission is granted for ACCESS_FINE_LOCATION then get the location candidates based on the device location
     * otherwise request permissions
     */
    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            getLocation()
            true
        } else {
            //TODO COMPLETED: Request Location permissions
            requestPermissions(
                    arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
            )
            false
        }
    }

    /**
     * checks whether the permission ACCESS_FINE_LOCATION is granted
     */
    private fun isPermissionGranted() : Boolean {
        //TODO COMPLETED: Check if permission is already granted and return (true = granted, false = denied/other)
        return requireContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /**
     * Source for ForeGround Locations with LocationServices
     * https://developer.android.com/training/location/permissions
     *
     * However, location was always null, and adapted to suggested code from
     * https://stackoverflow.com/questions/29441384/fusedlocationapi-getlastlocation-always-null
     */
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        //TODO COMPLETED: Get location from LocationServices
        //TODO COMPLETED: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address

        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    location?.let{
                        // create Address object from the received Location object
                        val address = geoCodeLocation(location)

                        // populate the address fields with the retrieved address data
                        binding.addressLine1.setText(address.line1)
                        binding.addressLine2.setText(address.line2)
                        binding.city.setText(address.city)
                        binding.zip.setText(address.zip)

                        // find the spinner position of the state by the retrieved location
                        val spinerPos = states.indexOf(address.state)
                        if (spinerPos >= 0){
                            // update the spinner to the retrieved state
                            binding.state.setSelection(spinerPos)
                        }

                        // fetch representatives for that address from the network
                        viewModel.getRepresentatives(address)
                    }
                }
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null)
    }

    /**
     * Provides an Address object based on the Location object provided by the GPS
     */
    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    /**
     * Hides the keyboard
     */
    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}

//TODO COMPLETED: Refresh adapters when fragment loads
@BindingAdapter("listRepresentativeData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Representative>?){
    val adapter = recyclerView.adapter as RepresentativeListAdapter
    adapter.submitList(data)
}