package com.example.doma

import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.content.Intent
import android.os.Vibrator
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tts: TextToSpeech
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale.getDefault()
            }
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                Toast.makeText(this@MainActivity, "Erro no reconhecimento de voz", Toast.LENGTH_SHORT).show()
            }

            override fun onResults(results: Bundle?) {
                val spokenText = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)
                processVoiceCommand(spokenText)
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        startListening()
    }

    private fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        speechRecognizer.startListening(intent)
    }

    private fun processVoiceCommand(command: String?) {
        command?.let {
            if (it.contains("alerta")) {
                playSafetyAlert()
            } else {
                tts.speak("Comando não reconhecido.", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }
    }

    private fun playSafetyAlert() {
        val alertMessage = "Atenção! Evacuar a área imediatamente."
        tts.speak(alertMessage, TextToSpeech.QUEUE_FLUSH, null, null)
        vibrator.vibrate(1000)
    }

    override fun onDestroy() {
        tts.shutdown()
        speechRecognizer.destroy()
        super.onDestroy()
    }
}
