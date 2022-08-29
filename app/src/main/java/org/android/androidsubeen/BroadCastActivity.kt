package org.android.androidsubeen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.android.androidsubeen.databinding.ActivityBroadCastBinding



@Suppress("DEPRECATION")
class BroadCastActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBroadCastBinding
    val br = SMSReceiver()
    private lateinit var filter: IntentFilter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBroadCastBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        filter = IntentFilter().apply {
            addAction("org.android.androidsubeen")
            //동적 할당 action
            addAction(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)
        }
        intent = getIntent()
        requirePerms()
        registerReceiver(br, filter)
        processedIntent(intent)
        super.onStart()
    }

    override fun onDestroy() {
        unregisterReceiver(br)
        super.onDestroy()
    }

    //문자열 set
    @SuppressLint("UnlocalizedSms")
    private fun processedIntent(intent: Intent?) {
        val str = intent?.getStrinagExtra("data")
        binding.etContent.setText(str)
        checkPermission()
        binding.btnSend.setOnClickListener {
            Log.e("log", binding.etPhone.text.toString())
            Log.e("log", binding.etContent.text.toString())
            val sms = SmsManager.getDefault()
            sms.sendTextMessage(binding.etPhone.text.toString(),null, binding.etContent.text.toString(),null,null)
           // Log.e("log", binding.etPhone.text.toString())
        }
    }


    //앱이 종료되지 않은 상태에서 메시지를 받을경우 Receiver 에서 startActivity에 의해 onNewIntent가 실행됨.
    override fun onNewIntent(intent: Intent?) {
        processedIntent(intent)
        super.onNewIntent(intent)
    }

    private fun requirePerms() {
        val permissions = arrayOf<String>(Manifest.permission.RECEIVE_SMS)
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, permissions, 1)
        }
    }
    private fun checkPermission(){
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS),101)
    }
}

