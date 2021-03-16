package com.example.android.politicalpreparedness.election.adapter

import androidx.databinding.InverseMethod
import java.text.SimpleDateFormat
import java.util.*


object DateStrConverters {
    @InverseMethod("strToDate")
    @JvmStatic
    fun dateToStr(value: Date?): String {
        return value?.toString() ?: ""
    }

    @JvmStatic
    fun strToDate(value: String): Date? {
        return SimpleDateFormat.getInstance().parse(value)
    }
}