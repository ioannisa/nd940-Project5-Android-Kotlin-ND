package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.*
import androidx.room.Database
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoterInfoViewModel(private val database: ElectionDatabase, private val election: Election) : ViewModel() {

    //TODO: Add live data to hold voter info



    //TODO: Add var and methods to populate voter info

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