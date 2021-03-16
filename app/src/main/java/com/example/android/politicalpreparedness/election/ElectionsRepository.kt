package com.example.android.politicalpreparedness.election

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionsRepository(private val database: ElectionDatabase) {

    var status = MutableLiveData<NETWORK_STATUS>(NETWORK_STATUS.INITIALIZING)

    suspend fun refreshElections(){
        withContext(Dispatchers.Main){
            try {
                status.value = NETWORK_STATUS.INITIALIZING

                Log.d("TEST", "1: ")
                // Get String json response via retrofit
                val electionsResponse = CivicsApi.retrofitService.getElections()
                Log.d("TEST", "1a: ")
                val elections = electionsResponse.body()?.elections

                Log.d("TEST", "2: ")

                // push the fetched results to the database
                withContext(Dispatchers.IO) {
                    if (elections != null) {
                        Log.d("TEST", "3: ")
                        database.electionDao.insertAll(*elections.toTypedArray())
                    }
                }

                Log.d("TEST", "4: ")
                status.value = NETWORK_STATUS.CONNECTED
            }
            catch (e: Exception){
                Log.d("TEST", "5: "+e.message)
                // Network Error (no internet)
                status.value = NETWORK_STATUS.DISCONNECTED
            }
        }
    }

}

enum class NETWORK_STATUS {INITIALIZING, CONNECTED, DISCONNECTED}