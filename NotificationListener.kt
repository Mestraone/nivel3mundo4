package com.example.doma

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import android.content.Context
import java.util.*

class NotificationListener : NotificationListenerService() {

    private lateinit var tts: TextToSpeech

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(applicationContext) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale.getDefault()
            }
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val notificationText = sbn?.notification?.tickerText?.toString() ?: return
        readNotification(notificationText)
    }

    private fun readNotification(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }
}
