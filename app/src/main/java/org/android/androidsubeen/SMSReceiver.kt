package org.android.androidsubeen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log


class SMSReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Log.e("contents", "11")

        if ("android.provider.Telephony.SMS_RECEIVED" == intent?.action) {
            val bundle = intent.extras
            val message = parseMessage(bundle)

            if (message.isNotEmpty()) {
                val contents = message[0]?.messageBody.toString()
                val number = contents.replace("[^0-9]".toRegex(), "")
                Log.e("log",number)
                //Log.e("contents", contents)
                sendToActivity(context, number)
            }
        }
    }

    private fun sendToActivity(context: Context?, str: String) {
        val intent = Intent(context, BroadCastActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("data", str)
        context?.startActivity(intent)
    }

    private fun parseMessage(bundle: Bundle?): Array<SmsMessage?> {
        val objs = bundle?.get("pdus") as Array<*>
        val messages = arrayOfNulls<SmsMessage>(objs.size)

        for (i in messages.indices) {
            val format = bundle.getString("format")
            messages[i] = SmsMessage.createFromPdu(objs[i] as ByteArray, format)
        }
        return messages
    }

}