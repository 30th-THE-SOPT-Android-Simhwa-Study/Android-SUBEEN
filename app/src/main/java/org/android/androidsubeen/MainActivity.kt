package org.android.androidsubeen

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.work.*
import org.android.androidsubeen.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {

            setOneTimeWorkRequest()
        }
    }


    @SuppressLint("EnqueueWork")
    private fun setOneTimeWorkRequest() {
        val workManger = WorkManager.getInstance(applicationContext)

        val data: Data = Data.Builder()
            .putInt(KEY_COUNT_VALUE, 125)
            .build()

        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = OneTimeWorkRequest.Builder(UPloadWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        val filteringRequest = OneTimeWorkRequest.Builder(FilteringWorker::class.java)
            .build()

        val compressingRequest = OneTimeWorkRequest.Builder(CompressingWorker::class.java)
            .build()

        val downloadingWorker = OneTimeWorkRequest.Builder(DownloadingWorker::class.java)
            .build()

//        val paralleWorks = mutableListOf<OneTimeWorkRequest>()
//        paralleWorks.add(filteringRequest)
//        paralleWorks.add(downloadingWorker)
//
//        workManger.beginWith(paralleWorks)
//            .then(compressingRequest)
//            .then(uploadRequest)
//            .enqueue()

        val chain1 = workManger.beginWith(compressingRequest).then(downloadingWorker)
        val chain2 = workManger.beginWith(filteringRequest).then(uploadRequest)
        WorkContinuation.combine(listOf(chain1, chain2)).enqueue()

        workManger.getWorkInfoByIdLiveData(uploadRequest.id)
            .observe(this) {
                binding.textview.text = it.state.name
                if (it.state.isFinished) {
                    Log.e("finish","finish")
                    val data = it.outputData
                    val message = data.getString(UPloadWorker.KEY_WORKER)
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                }
            }

    }

    companion object {
        const val KEY_COUNT_VALUE = "key_count"
    }
}