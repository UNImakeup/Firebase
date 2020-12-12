package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.RequiresApi;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;


public class Backbends extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private SensorEventListener proximitySensorListener;
    CountDownTimer countDownTimerBefore;
    CountDownTimer countDownTimer;
    ExerciseData exerciseData;
    int millisInFuture;
    int countDownInterval;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backbends);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        final TextView textview=(TextView) findViewById(R.id.textView4);
        final TextView backbendTimer = findViewById(R.id.backBendTimer);
        final BackbendExercise backbendExercise = new BackbendExercise(1); //Nok bare fjerne difficulty fra disse klasser, så man ikke skal skrive noget i contructor. Det skal alligevel i exerciseData klassen, hvis det er.
        final MediaPlayer haidokenSound = MediaPlayer.create(this, R.raw.haidoken); //Create sound
        final MediaPlayer bruhexplosionSound = MediaPlayer.create(this, R.raw.bruhexplosion); //Create sound
        final MediaPlayer yesSound = MediaPlayer.create(this, R.raw.yes); //Create sound
        exerciseData = ExerciseData.getInstance();


        if(proximitySensor == null){
            Toast.makeText(this, "Proximity sensor not available !", Toast.LENGTH_LONG).show();
            finish();
        }

        switch (exerciseData.getDifficulty()) {
            case 1:
                millisInFuture = 10000;
                countDownInterval = 1000;
                break;
            case 2:
                millisInFuture = 20000;
                countDownInterval = 1000;
                break;
            case 3:
                millisInFuture = 30000;
                countDownInterval = 1000;
                break;
        }

        countDownTimerBefore = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                backbendTimer.setText(millisUntilFinished/1000 + "");
            }

            @Override
            public void onFinish() {
                Toast.makeText(Backbends.this, "GO", Toast.LENGTH_SHORT).show();
                //pushupTimer.setText("");
                countDownTimer.start();
                onStop();

            }
        };
        countDownTimerBefore.start();

        countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                backbendTimer.setText(millisUntilFinished/1000 + " Seconds left");
            }

            @Override
            public void onFinish() {
                Toast.makeText(Backbends.this,"Workout Done",Toast.LENGTH_SHORT).show();
                exerciseData.addExercise(backbendExercise);
                Intent goHome = new Intent(Backbends.this, WorkoutDone.class);
                startActivity(goHome);
                onStop();
                finish();
            }
        };

        proximitySensorListener = new SensorEventListener() {
            boolean rep;

            //Tæller rygbøjninger
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float currentValue = sensorEvent.values[0];

                if(currentValue == 0.0){
                    rep = false;
                }
                if (currentValue == 5.0 && rep == false){
                    backbendExercise.addRep();
                    rep = true;
                }
                //Bare fjerne sensorværdien, have et billede der ændrer sig, og et tal over. Timer under billedet, der måske kunne være rundt.
                textview.setText(String.valueOf((backbendExercise.getReps() - 1))); //-1, fordi den starter på 1 af en eller anden grund.

                switch (backbendExercise.getReps()){
                    case 10:
                        haidokenSound.start();
                        break;
                    case 15:
                        bruhexplosionSound.start();
                        break;
                    case 20:
                        yesSound.start();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        sensorManager.registerListener(proximitySensorListener, proximitySensor, 2*1000*1000, 1000);
    }
    @Override
    protected void onStop(){
        super.onStop();
        sensorManager.unregisterListener(proximitySensorListener);
    }
    }