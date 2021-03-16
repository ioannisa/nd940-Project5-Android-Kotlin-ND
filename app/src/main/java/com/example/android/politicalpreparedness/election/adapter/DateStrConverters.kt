package com.example.android.politicalpreparedness.election.adapter

import androidx.databinding.InverseMethod
import java.text.SimpleDateFormat
import java.util.*


object DateStrConverters {
    @JvmStatic
    fun dateToStr(value: Date?): String {
        return value?.toString() ?: ""
    }
}