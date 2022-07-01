package org.android.androidsubeen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import org.android.androidsubeen.ProgressWorker.Companion.Progress
import org.android.androidsubeen.databinding.ActivityProgressBinding
import java.util.concurrent.TimeUnit

class ProgressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProgressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initWork()
    }

    private fun createConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .setRequiresCharging(false)
        .setRequiresBatteryNotLow(true)
        .build()

    private fun createWorkRequest() = PeriodicWorkRequestBuilder<ProgressWorker>(16, TimeUnit.MINUTES)
        .addTag("notify")
        .setConstraints(createConstraints())
        .build()

    private fun initWork() {
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "Work",
                ExistingPeriodicWorkPolicy.REPLACE,
                createWorkRequest()
            )


        WorkManager.getInstance(applicationContext)
            .getWorkInfosByTagLiveData("workmanager")
            .observe(this) {
                val progress = it[0].progress
                val value = progress.getInt(Progress, 0)
                binding.tvNumber.text = value.toString()
                binding.progressBar.progress = value
            }
    }

}