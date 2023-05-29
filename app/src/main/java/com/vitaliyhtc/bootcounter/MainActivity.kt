package com.vitaliyhtc.bootcounter

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.vitaliyhtc.bootcounter.application.BootCounterApp
import com.vitaliyhtc.bootcounter.data.DataStore
import com.vitaliyhtc.bootcounter.databinding.ActivityMainBinding
import com.vitaliyhtc.bootcounter.utils.openApplicationSettings
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val appInstance: BootCounterApp = BootCounterApp.instance
    private val dataStore: DataStore = appInstance.getDataStore()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupData()
        setupUI()
    }

    override fun onStart() {
        super.onStart()
        checkNotificationPermission()
    }

    private fun setupData() {
        dataStore.broadcastEventsUpdateListener { events ->
            if (events.isEmpty()) {
                binding.tvData.text = getString(R.string.no_boots_detected)
            } else {
                // TODO: implement as RecyclerView
                var counter = 0
                val resultBuilder = StringBuilder()
                events.forEach { event ->
                    resultBuilder.append("\n\n")
                    resultBuilder.append("${counter++}")
                    resultBuilder.append(event.toShortString())
                }

                binding.tvData.text = resultBuilder.toString()
            }
        }
    }

    private fun setupUI() {
        setupToolbar()

        binding.btnOpenAppSettings.setOnClickListener {
            openApplicationSettings(this)
        }
    }

    private fun setupToolbar() {
        with(binding) {
            toolbar.setTitle(R.string.app_name)
            toolbar.setTitleTextColor(getColor(R.color.text_color))
            setSupportActionBar(toolbar)
        }
    }

    // TODO: refactor to dialogs with rationale & all required things.
    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                binding.clPermission.isVisible = true
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            } else binding.clPermission.isVisible = false
        } else binding.clPermission.isVisible = false
    }

    companion object {
        private const val NOTIFICATION_PERMISSION_REQUEST_CODE = 230
        private const val NOTIFICATION_ID = 1
    }
}