package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

@Dao
interface ElectionDao {

    //TODO COMPLETED: Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg elections: Election)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(election: Election)

    //TODO COMPLETED: Add select all election query
    /**
     * The bellow query compares the election date (in milliseconds) to the provided date
     * The default date is gained from LocalDate, as to remove time from it, so it can be
     * comparable when we compare for elections that are happening on the same date as the system date
     *
     * Source: LocalDate to Date conversion
     * https://beginnersbook.com/2017/10/java-convert-localdate-to-date/
     */
    @Query("SELECT * FROM election_table WHERE electionDay >= :date ORDER BY electionDay DESC")
    fun getElections(date: Date = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())): LiveData<List<Election>>

    /**
     * For the followed elections we don't care if they are past elections.
     * As long as we have bookmarked them (saved them), they will appear
     */
    @Query("SELECT * FROM election_table WHERE id in (SELECT id FROM follow_election_table) ORDER BY electionDay DESC")
    fun getFollowedElections(): LiveData<List<Election>>

    //TODO COMPLETED: Add select single election query
    @Query("SELECT * FROM election_table WHERE id = :idElection")
    fun getElection(idElection: Int): Election?

    @Query("SELECT CASE id WHEN NULL THEN 0 ELSE 1 END FROM follow_election_table WHERE id = :idElection")
    fun isElectionFollowed(idElection: Int): LiveData<Int>

    @Query("INSERT INTO follow_election_table (id) VALUES(:idElection)")
    suspend fun followElection(idElection: Int)
    suspend fun followElection(election: Election){
        followElection(election.id)
    }

    //TODO COMPLETED: Add delete query
    @Delete
    fun deleteElection(election: Election)

    @Query("DELETE FROM follow_election_table WHERE id = :idElection")
    suspend fun unfollowElection(idElection: Int)
    suspend fun unfollowElection(election: Election){
        unfollowElection(election.id)
    }

    @Query("DELETE FROM election_table WHERE id = :idElection")
    fun deleteElection(idElection: Int)

    //TODO COMPLETED: Add clear query
    @Query("DELETE FROM election_table")
    fun clear()
}