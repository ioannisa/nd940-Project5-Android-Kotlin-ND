package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository(private val database: ElectionDatabase) {

    var status = MutableLiveData<NETWORK_STATUS>(NETWORK_STATUS.INITIALIZING)

    var electionsList: LiveData<List<Election>> = database.electionDao.getElections()
    var savedElectioinsList: LiveData<List<Election>> = database.electionDao.getFollowedElections()

    suspend fun refreshElections(){
        withContext(Dispatchers.Main){
            try {
                status.value = NETWORK_STATUS.INITIALIZING

                // Get String json response via retrofit
                val electionsResponse = CivicsApi.retrofitService.getElections()
                val elections = electionsResponse.body()?.elections

                // push the fetched results to the database
                withContext(Dispatchers.IO) {
                    if (elections != null) {
                        database.electionDao.insertAll(*elections.toTypedArray())
                    }
                }

                status.value = NETWORK_STATUS.CONNECTED
            }
            catch (e: Exception){
                // Network Error (no internet)
                status.value = NETWORK_STATUS.DISCONNECTED
            }
        }
    }

}

enum class NETWORK_STATUS {INITIALIZING, CONNECTED, DISCONNECTED}