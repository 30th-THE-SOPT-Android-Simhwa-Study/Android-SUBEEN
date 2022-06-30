package org.android.androidsubeen

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.*

class UPloadWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {

        try {

            val count = inputData.getInt(MainActivity.KEY_COUNT_VALUE, 0)

            for (i in 0 until count) {

                Log.i("kimsubeen", "uploadworker $i")
            }

            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = time.format(Date())

            val outPutData = Data.Builder()
                .putString(KEY_WORKER, currentDate)
                .build()

            return Result.success(outPutData)

        } catch (e: Exception) {

            return Result.failure()

        }

    }

    companion object {
        const val KEY_WORKER = "key_worker"
    }

}