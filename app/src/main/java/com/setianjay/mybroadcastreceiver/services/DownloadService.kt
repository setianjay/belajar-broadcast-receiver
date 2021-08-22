package com.setianjay.mybroadcastreceiver.services

import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.setianjay.mybroadcastreceiver.ui.MainActivity

class DownloadService: JobIntentService() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent != null){
            enqueueWork(this, this::class.java, 101, intent)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleWork(intent: Intent) {
        Log.d(TAG, "Download started")
        try {
            Thread.sleep(5000)

        }catch (e: InterruptedException){
            e.printStackTrace()
        }

        // if the service has finish, send Broadcast Message
        Intent(MainActivity.ACTION_DOWNLOAD_STATUS).also {
            it.putExtra(EXTRA_NUMBER, "082298061812")
            it.putExtra(EXTRA_MESSAGE, "Hello, this is Hari Setiaji... please save my contact")
            sendBroadcast(it)
        }
    }

    companion object{
        private val TAG = this::class.java.simpleName
        const val EXTRA_NUMBER = "extra_number"
        const val EXTRA_MESSAGE = "extra_message"
    }
}