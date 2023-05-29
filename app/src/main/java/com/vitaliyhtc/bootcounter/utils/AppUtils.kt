package com.vitaliyhtc.bootcounter.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat.startActivity
import com.vitaliyhtc.bootcounter.application.BootCounterApp

fun openApplicationSettings(activityContext: Activity) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", BootCounterApp.instance.packageName, null)
    intent.data = uri
    startActivity(activityContext, intent, null)
}