package org.android.androidsubeen

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class CompressingWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        try {
            for (i in 0..100) {
                Log.i("kimsubeen", "compressingworker $i")
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }
}