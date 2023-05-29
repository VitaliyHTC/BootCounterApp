package com.vitaliyhtc.bootcounter.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeFormatUtils {

    @Suppress("MemberVisibilityCanBePrivate")
    const val TIME_FORMAT_FULL = "MM/dd/yyyy HH:mm:ss"

    fun toDateTimeString(timestamp: Long): String {
        val formatter = SimpleDateFormat(TIME_FORMAT_FULL, Locale.US)
        return formatter.format(Date(timestamp))
    }
}