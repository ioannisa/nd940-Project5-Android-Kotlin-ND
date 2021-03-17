package com.example.android.politicalpreparedness.election

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.CivicsApiService
import java.lang.Appendable

//TODO COMPLETED: Create Factory to generate ElectionViewModel with provided election datasource
class ElectionsViewModelFactory(private val context: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
            return ElectionsViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}