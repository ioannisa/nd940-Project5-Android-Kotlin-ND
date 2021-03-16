package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    //TODO COMPLETED: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg elections: Election)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(election: Election)

    //TODO COMPLETED: Add select all election query
    @Query("SELECT * FROM election_table WHERE electionDay >= date('now') ORDER BY date(electionDay) DESC")
    fun getElections(): LiveData<List<Election>>

    @Query("SELECT * FROM election_table WHERE electionDay >= date('now') AND id in (SELECT id FROM follow_election_table) ORDER BY date(electionDay) DESC")
    fun getFollowedElections(): LiveData<List<Election>>

    //TODO COMPLETED: Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :idElection")
    fun getElection(idElection: Int): Election?

    //TODO COMPLETED: Add delete query
    @Delete
    fun deleteElection(election: Election)

    @Query("DELETE FROM election_table WHERE id = :idElection")
    fun deleteElection(idElection: Int)

    //TODO COMPLETED: Add clear query
    @Query("DELETE FROM election_table")
    fun clear()
}