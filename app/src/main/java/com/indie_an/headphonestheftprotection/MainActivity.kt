package com.indie_an.headphonestheftprotection

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var receiver: BroadcastReceiver? = null
    private var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_HEADSET_PLUG)

        val textView: TextView = findViewById(R.id.statusText)

        receiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if(intent != null && intent.action == Intent.ACTION_HEADSET_PLUG) {
                    val state: Int = intent.getIntExtra("state", -1)

                    mp = MediaPlayer()
                    mp!!.setAudioStreamType(AudioManager.STREAM_ALARM)
                    mp!!.isLooping = true

                    try {
                        mp!!.setDataSource(context, Uri.parse(
                            "android.resource://com.indie_an.headphonestheftprotection/"
                                    + R.raw.avast_virus_alert))
                        mp!!.prepare()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    when(state) {
                        0 -> {
                            textView.text = getString(R.string.alert_text)
                            mp!!.start()

                        }
                        1 -> {
                            textView.text = getString(R.string.listening_text)
                            mp!!.stop()
                            mp!!.release()
                        }
                    }
                }
            }
        }
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        if(receiver != null) {
            unregisterReceiver(receiver)
            receiver = null
        }

        super.onDestroy()
    }
}
