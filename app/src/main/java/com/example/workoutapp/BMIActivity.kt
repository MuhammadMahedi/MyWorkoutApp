package com.example.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Range
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_bmiactivity.*
import kotlinx.android.synthetic.main.activity_finish.*
import kotlin.concurrent.thread
import kotlin.math.ceil
import kotlin.math.pow

class BMIActivity : AppCompatActivity() {

    //var toggle=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmiactivity)

        setSupportActionBar(toolbar_bmi_activity)
        val actionbar= supportActionBar
        if(actionbar!=null){
            actionbar.setTitle("Calculate BMI")
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        calculate_button.setOnClickListener {
            calculateBMIinMetric()

        }
        calculate_button_US.setOnClickListener {
            calculateBMIinUS()
        }


        rgUnits.setOnCheckedChangeListener { group, checkedId ->
            //toggle *= (-1)
            when(checkedId){
                R.id.rbMetricunits->{
                    //actionbar!!.title = "metric"//checkedId.toString()
                    setLayoutForBMIinMetric()
                }
                //if(checkedId==R.id.ll_BMIinUS)
                R.id.rbUSunits->{
                    //actionbar!!.title = "US"//checkedId.toString()
                    setLayoutForBMIinUS()
                }
            }
        }
    }

    private fun calculateBMIinMetric(){
        var weight=(weight.text.toString()).toDoubleOrNull()
        var height=(height.text.toString()).toDoubleOrNull()

        if(weight==null||height==null){
            Toast.makeText(this, "Please enter the Height and weight properly",
                Toast.LENGTH_SHORT).show()
            tv_bmi_state.text=""
            tv_conclusion.text=""
            bmi_text.text=""
            return
        }else{

            height/=100
            var BMI= ceil(weight!!/height!!.pow(2)).toInt()
            bmi_text.text=BMI.toString()
            if(BMI<18){
                tv_bmi_state.text="Below Range"
                tv_conclusion.text="You need to take serious care of your health. " +
                        "Try to add more nutretious foods in your diet."
            }
            else if(BMI in 18..24){
                tv_bmi_state.text="Good Condition"
                tv_conclusion.text="Congratulation with your health. " +
                        "Your health is in a perfect state."
            }
            if(BMI>24){
                tv_bmi_state.text="Over Range"
                tv_conclusion.text="You need to take serious care of your health. " +
                        "Try to get some exercises and maintain a proper diet."
            }
        }
    }

    private fun calculateBMIinUS() {
        var weightPounds=weightInPounds.text.toString().toDoubleOrNull()
        var FeetInches=(heightInFeet.text.toString().toDoubleOrNull())
        var inch=(heightInInch.text.toString().toDoubleOrNull())
        if(inch==null){
            inch=0.0
        }

        if(weightPounds==null||FeetInches==null){
            Toast.makeText(this, "Please enter the Height and weight properly",
                Toast.LENGTH_SHORT).show()
            tv_bmi_state_US.text=""
            tv_conclusion_US.text=""
            bmi_text_US.text=""
            return
        }

        var bmiUs=(weightPounds!!/(FeetInches!!*12+inch!!).pow(2))*703
        bmiUs=kotlin.math.ceil(bmiUs)
        var bmi=bmiUs.toInt()

        bmi_text_US.text=bmi.toString()

        if(bmi<18){
            tv_bmi_state_US.text="Below Range"
            tv_conclusion_US.text="You need to take serious care of your health. " +
                    "Try to add more nutretious foods in your diet."
        }
        else if(bmi in 18..24){
            tv_bmi_state_US.text="Good Condition"
            tv_conclusion_US.text="Congratulation with your health. " +
                    "Your health is in a perfect state."
        }
        if(bmi>24){
            tv_bmi_state_US.text="Over Range"
            tv_conclusion_US.text="You need to take serious care of your health. " +
                    "Try to get some exercises and maintain a proper diet."
        }




    }

    private fun setLayoutForBMIinUS(){
        ll_BMIinUS.visibility=View.VISIBLE
        ll_BMIinMetric.visibility=View.INVISIBLE
        weight.text?.clear()
        height.text?.clear()
    }
    private fun setLayoutForBMIinMetric(){
        ll_BMIinMetric.visibility=View.VISIBLE
        ll_BMIinUS.visibility=View.INVISIBLE


    }
}