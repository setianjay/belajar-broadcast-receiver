package com.setianjay.mybroadcastreceiver

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.setianjay.mybroadcastreceiver.databinding.ActivitySmsReceiverBinding

class SmsReceiverActivity : AppCompatActivity() {
    private var binding: ActivitySmsReceiverBinding? = null

    private val senderNo by lazy { intent.getStringExtra(EXTRA_SMS_NO) }
    private val senderMessage by lazy { intent.getStringExtra(EXTRA_SMS_MESSAGE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySmsReceiverBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setupView()
        initListener()
    }

    private fun setupView(){
        binding?.tvFrom?.text = getString(R.string.from, senderNo)
        binding?.tvMessage?.text = senderMessage
    }

    private fun initListener(){
        binding?.btnClose?.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    companion object{
        const val EXTRA_SMS_NO = "extra_sms_no"
        const val EXTRA_SMS_MESSAGE = "extra_sms_message"
    }
}