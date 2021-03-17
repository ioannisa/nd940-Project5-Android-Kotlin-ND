package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.launch

//TODO COMPLETED: Construct ViewModel and provide election datasource
class ElectionsViewModel(application: Application): AndroidViewModel(application) {

    //TODO: Create live data val for upcoming elections
    private val repository = ElectionsRepository(ElectionDatabase.getInstance(application))

    //TODO COMPLETED: Create live data val for saved elections
    //TODO COMPLETED: Create val and functions to populate live data for upcoming elections from the API and saved elections from local database
    val electionsList: LiveData<List<Election>> = repository.electionsList
    val savedElectioinsList: LiveData<List<Election>> = repository.savedElectioinsList

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

    //TODO COMPLETED: Create functions to navigate to saved or upcoming election voter info
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