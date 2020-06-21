package com.jaykhatri.sensordata;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements  SensorEventListener {

    private SensorManager sensorManager;
    private Sensor mSensor,mLight;
    private  Vibrator v;
    private TextView xtv,ytv,ztv,cnt;
    private float[] gravity = new float[3];
    private  Integer count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = null;

        xtv= findViewById(R.id.x);
        ytv= findViewById(R.id.y);
        ztv= findViewById(R.id.z);
        cnt = findViewById(R.id.count);

        mLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        // Use the accelerometer.
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        } else{
            Log.d("MainActivity", "no accelerometer");
        }

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        final float alpha = 0.8f;
        float[] x = new float[3];

        gravity[0] = alpha * gravity[0] + (1 - alpha) * sensorEvent.values[0];
        gravity[1] = alpha * gravity[1] + (1 - alpha) * sensorEvent.values[1];
        gravity[2] = alpha * gravity[2] + (1 - alpha) * sensorEvent.values[2];

        x[0] = sensorEvent.values[0] - gravity[0];
        x[1] = sensorEvent.values[1] - gravity[1];
        x[2] = sensorEvent.values[2] - gravity[2];

        Log.d("MainActivity", "Sensor value changed: sensor value x: "+sensorEvent.values[0]+"y: "+sensorEvent.values[1]+"z: "+sensorEvent.values[2]);
        xtv.setText("x: " + x[0]);
        ytv.setText("y: " + sensorEvent.values[1]);
        ztv.setText("z: " + x[2]);
        if(Math.round(x[0]) == 0 && (Math.round(sensorEvent.values[1]) ==8 ||Math.round(sensorEvent.values[1]) ==9 || Math.round(sensorEvent.values[1]) ==10 )&& Math.round(x[2]) == 0){
            Log.d("mainActivity", "count: "+count);
            v.vibrate(200);
            count++;
            cnt.setText(""+count);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
