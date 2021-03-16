package com.example.android.politicalpreparedness.network.models

import android.os.Parcelable
import androidx.room.*
import com.squareup.moshi.*
import kotlinx.android.parcel.Parcelize
import java.util.*

/**
 * ROOM LEVEL Election data class (the Room Entity - table)
 *
 * To make it more efficient, we add an index to the electionDay Date field,
 * because we will select only current and future events in our selection and
 * to have a fast index retrieval based on date will be important
 */

@Parcelize
@Entity(
        tableName = "election_table",
        indices = arrayOf(Index(value = ["electionDay"])))
data class Election(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name")val name: String,
        @ColumnInfo(name = "electionDay")val electionDay: Date,
        @Embedded(prefix = "division_") @Json(name="ocdDivisionId") val division: Division
) : Parcelable

/**
 * ROOM helper table to hold the IDs of the elections to follow
 */
@Parcelize
@Entity(tableName = "follow_election_table")
data class FollowElection(
        @PrimaryKey val id: Int
) : Parcelable
