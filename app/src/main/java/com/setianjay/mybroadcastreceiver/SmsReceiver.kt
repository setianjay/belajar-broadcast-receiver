package com.setianjay.mybroadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.SmsMessage
import android.util.Log
import java.lang.Exception

class SmsReceiver : BroadcastReceiver() {

    /**
     * description      : function to get a Broadcast Message from other application, system android or own app
     * params in        : - context: Context        - intent: Intent
     * return           :
     * */
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val bundle = intent.extras

        try {
            if (bundle != null) {
                /*
                    Bundle dengan key "pdus" sudah merupakan standar yang digunakan oleh system
                */
                val pdusObj = bundle.get("pdus") as Array<*>
                for (aPdusObj in pdusObj) {
                    val currentMessage = getIncomeMessage(aPdusObj as Any, bundle)
                    val senderNumber = currentMessage.displayOriginatingAddress // get the sender sms number
                    val message = currentMessage.displayMessageBody // get the body of sms
                    Log.d(TAG, "sender num : $senderNumber")
                    Log.d(TAG, "message : $message")

                    // if sms has getting in android, the application will start intent for showing that message with SmsReceiverActivity
                    Intent(context, SmsReceiverActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        it.putExtra(SmsReceiverActivity.EXTRA_SMS_NO, senderNumber)
                        it.putExtra(SmsReceiverActivity.EXTRA_SMS_MESSAGE, message)
                        context.startActivity(it)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception smsReceiver $e")
        }
    }

    /**
     * description      : for get the current message
     * params in        : - aObject: Any        - bundle: Bundle
     * return           : SmsMessage
     * */
    private fun getIncomeMessage(aObject: Any, bundle: Bundle): SmsMessage {
        val format = bundle.getString("format")
        // check the user os version
        return if (Build.VERSION.SDK_INT >= 23)
            SmsMessage.createFromPdu(aObject as ByteArray, format)
        else SmsMessage.createFromPdu(aObject as ByteArray)
    }

    companion object {
        private val TAG = SmsReceiver::class.java.simpleName
    }
}