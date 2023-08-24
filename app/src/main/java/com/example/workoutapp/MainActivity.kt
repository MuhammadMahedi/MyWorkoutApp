package com.example.workoutapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)


        //window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN
        llStart.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            //val intent = Intent(this, FinishActivity::class.java)
            startActivity(intent)
            speakOut("Your Workout will start in 10 seconds")

        }
        iv_appLogo.setOnClickListener {
            speakOut("Welcome to Our 7 Minute Workout Application")
        }

        bmi_button.setOnClickListener {
            val bmiIntent=Intent(this,BMIActivity::class.java)
            startActivity((bmiIntent))
        }
        history_button.setOnClickListener {
            val historyIntent=Intent(this,HistoryActivity::class.java)
            startActivity(historyIntent)
        }

    }

    private fun speakOut(exercise: String) {
        tts!!.speak(exercise, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onInit(status: Int) {
        val result =tts!!.setLanguage(Locale.US)

    }
}