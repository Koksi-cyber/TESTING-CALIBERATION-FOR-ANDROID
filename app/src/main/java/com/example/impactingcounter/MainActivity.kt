package com.example.impactingcounter
// 05 June 2021
// Testing proximity sensor for android

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate


class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var counter: Int = 0
    private lateinit var square: TextView
    var number = 0








    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Keeps phone in light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        square = findViewById(R.id.tv_square)


        setUpSensorStuff()
    }


    private fun setUpSensorStuff() {
        // Create the sensor manager
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        // Specify the sensor you want to listen to
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_NORMAL

            )
        }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        // Checks for the sensor we have registered
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            //Log.d("Main", "onSensorChanged: sides ${event.values[0]} front/back ${event.values[1]} ")

            // Sides = Tilting phone left(10) and right(-10)
            val sides = event.values[0]

            // Up/Down = Tilting phone up(10), flat (0), upside-down(-10)
            val upDown = event.values[1]

            square.apply {
                rotationX = upDown * 3f
                rotationY = sides * 3f
                rotation = -sides
                translationX = sides * -10
                translationY = upDown * 10
            }
            number +=1



            // Changes the colour of the square if it's completely flat
            val color = if (upDown.toInt() == 0 && sides.toInt() == 0 ) {


            square.text = number.toString()

//            } else if (upDown.toInt() >= 6 && sides.toInt() >= 6) {
//                square.text = "you are holding the phone"
            } else {
              square.text =" please drop me down"
            }
            //square.text(color as Int)
            /**square.text =
                "up/down ${upDown.toInt()}\nleft/right ${sides.toInt()}"+
                        "\n${increments(movement1(upDown), movement2(sides))}\n"**/

        }


    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        return
    }

    override fun onPause() {
        sensorManager.unregisterListener(this)
        super.onPause()
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
}
