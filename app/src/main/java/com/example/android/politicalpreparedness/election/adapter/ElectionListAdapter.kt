package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.RowElectionBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener): ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback()) {
    //TODO COMPLETED: Create ElectionDiffCallback
    class ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
        // Same item if the items share the same id
        override fun areItemsTheSame(oldItem: Election, newItem: Election)    = (oldItem.id == newItem.id)

        // Same contents if dataclasses' equality is the same
        override fun areContentsTheSame(oldItem: Election, newItem: Election) = (oldItem == newItem)
    }

    //TODO COMPLETED: Create ElectionListener
    class ElectionListener(val clickListener: (election: Election) -> Unit) {
        fun onClick(election: Election) = clickListener(election)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        return ElectionViewHolder.from(parent)
    }

    //TODO COMPLETED: Bind ViewHolder
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        val election = getItem(position)
        holder.bind(clickListener, election)
    }

    //TODO COMPLETED: Create ElectionViewHolder
    class ElectionViewHolder private constructor(val binding: RowElectionBinding) : RecyclerView.ViewHolder(binding.root) {
        // layout binding -> row_election.xml
        fun bind(clickListener: ElectionListener, election: Election){
            binding.election = election
            binding.clickListener = clickListener

            // performance boost
            binding.executePendingBindings()
        }

        //TODO COMPLETED: Add companion object to inflate ViewHolder (from)
        companion object {
            fun from(parent: ViewGroup): ElectionViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowElectionBinding.inflate(layoutInflater, parent, false)
                return ElectionViewHolder(binding)
            }
        }
    }
}