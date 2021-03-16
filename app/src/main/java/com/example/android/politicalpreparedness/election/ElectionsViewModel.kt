package com.example.android.politicalpreparedness.election

import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.CivicsApiService
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

//TODO: Construct ViewModel and provide election datasource
class ElectionsViewModel(database: ElectionDatabase): ViewModel() {

    //TODO: Create live data val for upcoming elections
    private val repository = ElectionsRepository(database)

    init{
        refreshData()
    }

    fun refreshData(){
        viewModelScope.launch {
            repository.apply {
                refreshElections()
            }
        }
    }

    // Depending on the period to fetch, ask the Repository to fetch the period items from the DB
    val electionsList = database.electionDao.getElections()

    //TODO: Create live data val for saved elections

    //TODO: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database

    //TODO: Create functions to navigate to saved or upcoming election voter info

    private val _navigateToElectionDetail = MutableLiveData<Election>()
    val navigateToElectionDetail
        get() = _navigateToElectionDetail

    fun onElectionItemClicked(election: Election) {
        _navigateToElectionDetail.value = election
    }

    fun onElectionItemNavigated() {
        _navigateToElectionDetail.value = null
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Election>?){
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}