package com.example.sahourangleconverter

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.sahourangleconverter.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate((layoutInflater))
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("mypref", 0)
        binding.tbStartingSection.setText(sharedPref.getString("StartSector", "1"))
        binding.tbStartingvalue.setText(sharedPref.getString("StartValue", "0"))
        var img = getDrawable(R.drawable.ic_baseline_rotate_right_24)

        binding.btnSaveDefault.setOnClickListener {
            val sharedPref = getSharedPreferences("mypref", 0)
            val editor = sharedPref.edit()
            editor.putString("StartSector", binding.tbStartingSection.getText().toString());
            editor.putString("StartValue", binding.tbStartingvalue.getText().toString());
            editor.commit();
        }

        binding.button3.setOnClickListener {
            var jsonObject= JSONObject()
            val dialPositions = JSONArray()
            var myHours: Int = 0
            var myMinutes: Int =0
            var myStartSector: Int  = 0
            var myStartValue: Float = 0f
            var myDec: Float = 0f
            var decClockwise: Boolean = false
            var decRotation = "Counterclockwise"

            try{
                myHours =  binding.tbHours.getText().toString().toInt()
                if(myHours < 0 || myHours > 23){
                    binding.tbHours.setError("Hours have to be 0 to 23")
                    return@setOnClickListener
                }
            }catch(e: Exception){
                binding.tbHours.setError("Value has to be a Integer")
                return@setOnClickListener
            }

            try{
                myMinutes = binding.tbMinutes.getText().toString().toInt()
                if(myMinutes < 0 || myMinutes > 59){
                    binding.tbMinutes.setError("Minutes have to be 0 to 59")
                    return@setOnClickListener
                }
            }catch(e: Exception){
                binding.tbMinutes.setError("Value has to be a Integer")
                return@setOnClickListener
            }


            try{
                myStartSector = binding.tbStartingSection.getText().toString().toInt()
                if(myStartSector < 1 || myStartSector > 12){
                    binding.tbStartingSection.setError("Sector has to be 1 to 12")
                    return@setOnClickListener
                }
            }catch(e: Exception){
                binding.tbStartingSection.setError("Value has to be a Integer")
                return@setOnClickListener
            }

            try{
                myStartValue = binding.tbStartingvalue.getText().toString().toFloat()
                if(myStartValue < 0 || myStartValue > 14){
                    binding.tbStartingvalue.setError("Markings have to be 0 to 14")
                    return@setOnClickListener
                }
            }catch(e: Exception){
                binding.tbStartingvalue.setError("Value has to be a Decimal Number")
                return@setOnClickListener
            }

            try{
                myDec = binding.tbDEC.getText().toString().toFloat()
                if(myDec < -90 || myDec > 90){
                    binding.tbDEC.setError("Dec has to be -90 to 90")
                    return@setOnClickListener
                }
            }catch(e: Exception){
                binding.tbDEC.setError("Value has to be a Number")
                return@setOnClickListener
            }

            if(myDec < 0 ){
                decClockwise = true
            }else{
                decClockwise = false
            }

            if(myHours > 12){
                myHours -= 12
                decClockwise = !decClockwise
            }

            var LastSection: Int = myStartSector
            var LastValue: Float  = myStartValue
            var CurrentSection: Int = 0
            var CurrentValue: Float = 0f

            jsonObject.put("Section",LastSection)
            jsonObject.put("Value",LastValue)
            dialPositions.put(jsonObject)



            for (i in 0..360) {

                if (LastSection == 12 && LastValue == 14.5f) {
                    CurrentSection = 1;
                    CurrentValue = 0f
                }
                else if (LastValue == 14.5f){
                    CurrentSection = LastSection + 1
                    CurrentValue = 0f
                }
                else
                {
                    CurrentSection = LastSection
                    CurrentValue = LastValue + 0.5f
                }
                var jsonObject= JSONObject()
                jsonObject.put("Section",CurrentSection)
                jsonObject.put("Value",CurrentValue)
                dialPositions.put(jsonObject)
                LastSection = CurrentSection
                LastValue = CurrentValue

            }

            val Dialpos: Int = (((myHours *60) + myMinutes)/4)
            Log.d("DialPos", Dialpos.toString());
            val currentIndex = dialPositions.getJSONObject(Dialpos)
            val PrintSector = currentIndex.optString("Section")
            val PrintValue = currentIndex.optString("Value")

            binding.ResultSector.setText("${PrintSector}")
            binding.ResultMarking.setText("${PrintValue}")
            if(decClockwise){
                img = getDrawable(R.drawable.ic_baseline_rotate_right_32)
            }else{
                img = getDrawable(R.drawable.ic_baseline_rotate_left_32)
            }
            binding.ResultAngle.setCompoundDrawablesWithIntrinsicBounds(null,null,null,img)
            binding.ResultContainer.visibility = View.VISIBLE

        }
    }
}