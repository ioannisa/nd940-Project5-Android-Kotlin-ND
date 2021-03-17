package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Election


class ElectionsFragment: Fragment() {

    //TODO COMPLETED: Declare ViewModel
    private lateinit var viewModel: ElectionsViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        //TODO COMPLETED: Add binding values
        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this

        //TODO: Add ViewModel values and create ViewModel
        val viewModelFactory = ElectionsViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ElectionsViewModel::class.java)
        binding.viewModel = viewModel

        //TODO COMPLETED: Initiate recycler adapters
        //TODO COMPLETED: Populate recycler adapters

        // Setup Recycler View for upcoming elections
        val adapterUpcomingElections = ElectionListAdapter(ElectionListAdapter.ElectionListener {
            viewModel.navigateToElectionDetail
            viewModel.onElectionItemClicked(it)
        })
        binding.rvUpcomingElections.adapter = adapterUpcomingElections

        // Setup Recycler View for saved elections
        val adapterSavedElections = ElectionListAdapter(ElectionListAdapter.ElectionListener {
            viewModel.navigateToElectionDetail
            viewModel.onElectionItemClicked(it)
        })
        binding.rvSavedElections.adapter = adapterSavedElections

        //TODO COMPLETED: Link elections to voter info
        viewModel.navigateToElectionDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                // clear the event
                viewModel.onElectionItemNavigated()

                // navigate to detail screen
                this.findNavController().navigate(
                        ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(it))
            }
        })

        return binding.root

    }
}

//TODO COMPLETED: Refresh adapters when fragment loads
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Election>?){
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)
}