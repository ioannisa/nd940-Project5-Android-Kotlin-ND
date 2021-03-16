package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter


class ElectionsFragment: Fragment() {

    //TODO COMPLETED: Declare ViewModel
    //private val viewModel by viewModels<ElectionsViewModel> {
    //    ElectionsViewModelFactory(requireContext())
    //}
    private lateinit var viewModel: ElectionsViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this


        //TODO: Add ViewModel values and create ViewModel
        val viewModelFactory = ElectionsViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ElectionsViewModel::class.java)
        binding.viewModel = viewModel

        //TODO: Add binding values

        //TODO: Link elections to voter info

        //TODO: Initiate recycler adapters

        //TODO: Populate recycler adapters
        // Setup Recycler View
        val adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
            viewModel.navigateToElectionDetail
            viewModel.onElectionItemClicked(it)
        })
        binding.rvUpcomingElections.adapter = adapter
        binding.rvSavedElections.adapter = adapter

        return binding.root

    }

    //TODO: Refresh adapters when fragment loads

}