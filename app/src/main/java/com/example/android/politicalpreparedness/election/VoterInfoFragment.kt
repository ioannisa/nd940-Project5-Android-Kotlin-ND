package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding

class VoterInfoFragment : Fragment() {

    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        //TODO COMPLETED: Add binding values
        val binding = FragmentVoterInfoBinding.inflate(inflater)
        val selectedElection = VoterInfoFragmentArgs.fromBundle(requireArguments()).selectedElection

        binding.lifecycleOwner = this
        binding.election = selectedElection

        //TODO COMPLETED: Add ViewModel values and create ViewModel
        val viewModelFactory = VoterInfoViewModelFactory(requireActivity().application, selectedElection)
        viewModel = ViewModelProvider(this, viewModelFactory).get(VoterInfoViewModel::class.java)
        binding.viewModel = viewModel


        //TODO COMPLETED: Populate voter info -- hide views without provided data. --> Implemented at XML with DataBinding
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */


        //TODO COMPLETED: Handle loading of URLs
        viewModel.url.observe(viewLifecycleOwner, { url ->
            url?.let{
                viewModel.setUrl(null)
                loadUrl(url)
            }
        })

        //TODO COMPLETED: Handle save button UI state      --> Implemented at ViewModel
        //TODO COMPLETED: cont'd Handle save button clicks --> Implemented at ViewModel

        return binding.root
    }

    //TODO COMPLETED: Create method to load URL intents
    private fun loadUrl(url: String) {
        val implicit = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(implicit)
    }
}