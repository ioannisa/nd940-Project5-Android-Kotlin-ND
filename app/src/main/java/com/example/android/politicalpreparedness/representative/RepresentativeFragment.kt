package com.example.android.politicalpreparedness.representative

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.FragmentLaunchBinding
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.ElectionsViewModelFactory
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.model.Representative
import java.util.Locale

class RepresentativeFragment : Fragment() {

    companion object {
        //TODO COMPLETED: Add Constant for Location request
        private const val ACCESS_FINE_LOCATION = 1
    }

    //TODO COMPLETED: Declare ViewModel
    private lateinit var viewModel: RepresentativeViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        //TODO COMPLETED: Establish bindings
        val binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        //TODO COMPLETED: Add ViewModel values and create ViewModel
        val viewModelFactory = RepresentativeViewModelFractory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(RepresentativeViewModel::class.java)
        binding.viewModel = viewModel

        //TODO: Define and assign Representative adapter
        //TODO: Populate Representative adapter
        val adapterRepresentatives = RepresentativeListAdapter()
        binding.rvRepresentatives.adapter = adapterRepresentatives

        viewModel.getRepresentatives(getManualAddress())

        //TODO: Establish button listeners for field and location search
        return binding.root
    }

    fun getManualAddress(): Address{
        //hideKeyboard()

        return Address(
                line1 = "3324 Eagle Way",
                line2 = null,
                city = "Rosamond",
                state = "CA",
                zip = "93560"
        )
    }

// TODO UNCOMMENT:
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        //TODO: Handle location permission result to get location on permission granted
//    }
//
//    private fun checkLocationPermissions(): Boolean {
//        return if (isPermissionGranted()) {
//            true
//        } else {
//            //TODO: Request Location permissions
//            false
//        }
//    }
//
//    private fun isPermissionGranted() : Boolean {
//        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
//    }
//
//    private fun getLocation() {
//        //TODO: Get location from LocationServices
//        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
//    }
//
//    private fun geoCodeLocation(location: Location): Address {
//        val geocoder = Geocoder(context, Locale.getDefault())
//        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
//                .map { address ->
//                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
//                }
//                .first()
//    }

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