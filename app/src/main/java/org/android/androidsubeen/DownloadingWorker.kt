package org.android.androidsubeen

import android.annotation.SuppressLint
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*

class DownloadingWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    @SuppressLint("SimpleDateFormat")
    override fun doWork(): Result {
        return try {
            for (i in 0..3000) {
                Log.i("kimsubeen", "downloading $i")
            }
            val time = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
            val currentDate = time.format(Date())
            Log.i("kimsubeen-date", "download-date $currentDate")
             Result.success()
        } catch (e: Exception) {
             Result.failure()
        }
    }
}