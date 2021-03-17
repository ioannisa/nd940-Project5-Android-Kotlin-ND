package com.example.android.politicalpreparedness.election

import androidx.lifecycle.*
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class VoterInfoViewModel(private val database: ElectionDatabase, private val election: Election) : ViewModel() {

    //TODO COMPLETED: Add live data to hold voter info
    val voterInfo = MutableLiveData<VoterInfoResponse>()

    private val _url = MutableLiveData<String?>()
    val url: LiveData<String?>
        get() = _url

    fun setUrl(url: String?){
        _url.value = url
    }

    //TODO: Add var and methods to populate voter info

    /**
     * Helper method to produce address to pass to retrofit for voter info
     */
    private fun getDivisionAdr(division: Division): String{
        val result = StringBuilder()

        if (division.state   != "") result.append(division.state)
        if (division.country != ""){
            if (result.toString()!="")
                result.append(", ")

            result.append(division.country)
        }

        return result.toString()
    }

    private suspend fun fetchVotersInfo() = withContext(Dispatchers.IO) {
        val response = CivicsApi.retrofitService.getVoterInfo(getDivisionAdr(election.division), election.id)

        if (response.isSuccessful) {
            voterInfo.postValue(response.body())
        }
    }

    init{
        viewModelScope.launch {
            fetchVotersInfo()
        }
    }

    //TODO: Add var and methods to support loading URLs

    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status
    private val _isElectionFollowed: LiveData<Int>
        get() = database.electionDao.isElectionFollowed(election.id)

    /**
     * Transformation map to produce boolean result about if we follow the election or not
     */
    val isElectionFollowed =
            Transformations.map(_isElectionFollowed) { followValue ->
                followValue?.let {
                    followValue == 1
                }
            }

    /**
     * Follow / Unfollow the selected election
     */
    fun onFollowButtonClick(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (isElectionFollowed.value == true) {
                    database.electionDao.unfollowElection(election)
                } else {
                    database.electionDao.followElection(election)
                }
            }
        }
    }

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */

}