package com.example.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_bmiactivity.*
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setSupportActionBar(toolbar_history_activity)
        val actionbar= supportActionBar
        if(actionbar!=null){
            actionbar.setTitle("HISTORY")
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_history_activity.setNavigationOnClickListener {
            onBackPressed()
        }
        getAllCompletedDates()
    }

    fun getAllCompletedDates(){
        val dbHandler = SqliteOpenHelper(this,null)
        val allCompletedDateList=dbHandler.getAllCompletedDateList()

        if(allCompletedDateList.size>0){
            tvHistory.visibility=View.VISIBLE
            rvHistory.visibility=View.VISIBLE
            tvNoDataAvailable.visibility=View.GONE

            rvHistory.layoutManager=LinearLayoutManager(this)
            val historyAdapter =HistoryAdapter(this, allCompletedDateList)
            rvHistory.adapter=historyAdapter
        }else{
            tvHistory.visibility=View.GONE
            rvHistory.visibility=View.GONE
            tvNoDataAvailable.visibility=View.VISIBLE
        }

    }
}