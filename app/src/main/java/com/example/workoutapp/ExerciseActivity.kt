package com.example.workoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation.*
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener{

    private var tts:TextToSpeech?=null
    private var player:MediaPlayer?=null
    private var exerciseAdapter:ExerciseStatusAdapter?=null

    private var restTimer:CountDownTimer?=null
    private var restProgress=0
    private var exerciseTimer:CountDownTimer?=null
    private var exerciseProgress=0
    private var workoutDone:Boolean=false

    private var exerciseList:ArrayList<ExerciseModel>?=null
    private var currentExercisePosition= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        tts= TextToSpeech(this,this)

        setSupportActionBar(toolbar_exercise_activity)
        val actionbar= supportActionBar
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_exercise_activity.setNavigationOnClickListener {
            //onBackPressed()
            customDialogForBackButton()

        }

        exerciseList= Constants.defaultExerciseList()
        setupRestView()
        setupExerciseStatusRecyclerView()


    }

    override fun onDestroy() {
        if(restTimer!=null){
            restTimer!!.cancel()
            restProgress=0
        }

        if(exerciseTimer!=null){
            exerciseTimer!!.cancel()
            exerciseProgress=0
        }

        if(tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player!=null){
            player!!.stop()
        }

        super.onDestroy()
    }

    private fun setRestProgressBar(){
        progressBar.progress=restProgress
        restTimer = object : CountDownTimer(10000,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress=10-restProgress
                tvTimer.text=(10-restProgress).toString()
            }

            override fun onFinish() {
                //Toast.makeText(this@ExerciseActivity, "Here now we will start the exercise",
                    //Toast.LENGTH_SHORT).show()
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                currentExercisePosition++
                setupExerciseView()

            }

        }.start()

        }

    private fun setupRestView(){

        try {
            player=MediaPlayer.create(applicationContext,R.raw.press_start)
            player!!.isLooping=false
            player!!.start()
        }catch (e:Exception){
            e.printStackTrace()
        }

        speakOut("Take rest for a few moments") //activity start howa sathe sathe e speak kore na , jekono action hoye gele trpr theke speak kore


        llExerciseView.visibility=View.GONE
        llRestView.visibility= View.VISIBLE

        if(restTimer!=null){
            restTimer!!.cancel()
            restProgress=0
        }

        tv_upcomingExercise.text = exerciseList!![currentExercisePosition+1].getName()

        setRestProgressBar()
    }

    private fun speakOut(exercise: String) {
        tts!!.speak(exercise,TextToSpeech.QUEUE_FLUSH,null,"")
    }


    private fun setExerciseProgressBar(){
        progressBarEx.progress=exerciseProgress
        exerciseTimer = object : CountDownTimer(30000,1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                progressBarEx.progress=30-exerciseProgress
                tvTimerEx.text=(30-exerciseProgress).toString()
            }

            override fun onFinish() {
                if(currentExercisePosition< (exerciseList?.size!! -1)){

                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }else{
                    //speakOut("Congratulation you did it")
                    workoutDone=true
                    Toast.makeText(this@ExerciseActivity, "Congratulation you did it",
                        Toast.LENGTH_SHORT).show()

                    finish()
                    val intent=Intent(this@ExerciseActivity,
                        FinishActivity::class.java)
                    startActivity(intent)
                }


            }

        }.start()

    }


    private fun setupExerciseView(){
        llRestView.visibility= View.GONE
        llExerciseView.visibility=View.VISIBLE
        if(exerciseTimer!=null){
            exerciseTimer!!.cancel()
            exerciseProgress=0
        }
        setExerciseProgressBar()
        ivImage.setImageResource(exerciseList!![currentExercisePosition].getImage())
        tvExerciseName.text = exerciseList!![currentExercisePosition].getName()
        speakOut(exerciseList!![currentExercisePosition].getName())

    }

    override fun onInit(status: Int) {
        if(status==TextToSpeech.SUCCESS){
            val result =tts!!.setLanguage(Locale.US)

            if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","The Language is not supported in your device")
            }
        }
        else{
            Log.e("TTS","Initialization Failed")
        }
    }

    private fun setupExerciseStatusRecyclerView(){
        rvExerciseStatus.layoutManager=LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!,this)
        rvExerciseStatus.adapter =exerciseAdapter
    }
    private fun customDialogForBackButton(){
        val customDialog=Dialog(this)

        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)

        customDialog.yes_button.setOnClickListener {
            finish()
            customDialog.dismiss()
        }
        customDialog.no_button.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()

    }

}
