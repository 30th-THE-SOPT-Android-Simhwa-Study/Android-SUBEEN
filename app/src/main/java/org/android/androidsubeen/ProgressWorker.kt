package org.android.androidsubeen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf


class ProgressWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    val CHANNEL_ID = "channel id"
    override suspend fun doWork(): Result {
        notifyProgress()
        return try {
            for (i in 0..1000) {
                Log.i("kimsubeen", "progress $i")
                val progress = workDataOf(Progress to i)
                setProgress(progress)
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun notifyProgress() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, 0)


        var builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("My notification")
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Much longer text that cannot fit one line...")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_NAME
            val descriptionText = CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            notificationManager.createNotificationChannel(channel)
        }
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(0, builder.build())
        }

    }


    companion object {
        const val Progress = "Progress"
        const val CHANNEL_NAME = "channel id"
        const val CHANNEL_DESCRIPTION = "channel description"
    }
}
