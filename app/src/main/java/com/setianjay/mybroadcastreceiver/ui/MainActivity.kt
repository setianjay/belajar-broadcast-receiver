package com.setianjay.mybroadcastreceiver.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.setianjay.mybroadcastreceiver.PermissionManager
import com.setianjay.mybroadcastreceiver.R
import com.setianjay.mybroadcastreceiver.databinding.ActivityMainBinding
import com.setianjay.mybroadcastreceiver.services.DownloadService

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityMainBinding? = null
    private lateinit var downloadReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initReceiver()
        initListener()
    }

    /**
     * description      : function for initialize Broadcast Receiver
     * params in        :
     * return           :
     * */
    private fun initReceiver(){
        downloadReceiver = object: BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.d(TAG, "Download finished")
                val number = intent?.getStringExtra(DownloadService.EXTRA_NUMBER)
                val message = intent?.getStringExtra(DownloadService.EXTRA_MESSAGE)

                Intent(context, SmsReceiverActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    it.putExtra(SmsReceiverActivity.EXTRA_SMS_NO, number)
                    it.putExtra(SmsReceiverActivity.EXTRA_SMS_MESSAGE, message)
                    context?.startActivity(it)
                }
            }
        }

        val downloadIntentFilter = IntentFilter(ACTION_DOWNLOAD_STATUS) // to set IntentFilter of the Receiver
        registerReceiver(downloadReceiver, downloadIntentFilter) // register Broadcast Receiver
    }

    private fun initListener(){
        binding?.btnPermission?.setOnClickListener(this)
        binding?.btnDownload?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_permission -> {
                PermissionManager.checkPermission(
                    this,
                    Manifest.permission.RECEIVE_SMS,
                    PERMISSION_REQUEST_CODE
                )
            }
            R.id.btn_download -> {
                Intent(this, DownloadService::class.java).also {
                    startService(it)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()){
            for (i in grantResults.indices){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, getString(R.string.permission_accepted), Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, getString(R.string.permission_declined), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        unregisterReceiver(downloadReceiver) // unregister Broadcast Receiver
    }

    companion object{
        private val TAG = this::class.java.simpleName
        private const val PERMISSION_REQUEST_CODE = 101
        const val ACTION_DOWNLOAD_STATUS = "download_status"
    }
}