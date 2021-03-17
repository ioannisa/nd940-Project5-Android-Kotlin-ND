package com.example.android.politicalpreparedness.representative

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.model.Representative
import com.example.android.politicalpreparedness.representative.model.representatives
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RepresentativeViewModel(application: Application): AndroidViewModel(application) {

    //TODO: Establish live data for representatives and address
    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() =_address

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    //TODO: Create function to fetch representatives from API from a provided address
    fun getRepresentatives(address: Address) = viewModelScope.launch(Dispatchers.IO) {
        _address.postValue(address)

        val response = CivicsApi.retrofitService.getRepresentatives(address.toFormattedString())
        _representatives.postValue(response.body()?.representatives)
    }

    //TODO: Create function get address from geo location

    //TODO: Create function to get address from individual fields
}
