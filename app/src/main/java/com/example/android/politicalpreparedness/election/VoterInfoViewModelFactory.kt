package com.example.android.politicalpreparedness.election

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election

//TODO COMPLETED: Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(private val context: Context, private val election: Election) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            return VoterInfoViewModel(ElectionDatabase.getInstance(context), election) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}