package com.example.firebase;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.RequiresApi;

import android.annotation.SuppressLint;
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
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
    int delayMillis;
    int i = 0;
    Button skipBackbends;

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backbends);


        //actionbar hide
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();


        final Handler handler = new Handler();
        final ProgressBar progressBarBackbends = findViewById(R.id.progressBarBackbends);
        progressBarBackbends.setVisibility(View.INVISIBLE);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        final TextView textview=(TextView) findViewById(R.id.textView4);
        textview.setVisibility(View.INVISIBLE);
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
        skipBackbends = findViewById(R.id.skipBackbends);
        skipBackbends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                countDownTimerBefore.cancel();
                ExerciseData exerciseData = ExerciseData.getInstance();
                exerciseData.addExercise(backbendExercise);
                Intent goHome = new Intent(Backbends.this, WorkoutDone.class);
                startActivity(goHome);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                onStop();
                finish();
            }
        });


        switch (exerciseData.getDifficulty()) {
            case 1:
                millisInFuture = 10500;
                countDownInterval = 1000;
                delayMillis = 90;
                break;
            case 2:
                millisInFuture = 20500;
                countDownInterval = 1000;
                delayMillis = 190;
                break;
            case 3:
                millisInFuture = 30500;
                countDownInterval = 1000;
                delayMillis = 270;
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
                sensorManager.registerListener(proximitySensorListener, proximitySensor, 2*1000*1000, 1000);
                countDownTimer.start();
                progressBarBackbends.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(1<=100){
                            progressBarBackbends.setProgress(i);
                            i++;
                            handler.postDelayed(this,delayMillis);

                        }else{
                            handler.removeCallbacks(this);
                        }
                    }
                },1000);

                onStop();
            }


        };
        countDownTimerBefore.start();

        countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                backbendTimer.setText(millisUntilFinished/1000 + "");
                textview.setVisibility(View.VISIBLE);


            }

            @Override
            public void onFinish() {
                Toast.makeText(Backbends.this,"Workout Done!!!",Toast.LENGTH_SHORT).show();
                exerciseData.addExercise(backbendExercise);
                Intent goHome = new Intent(Backbends.this, WorkoutDone.class);
                startActivity(goHome);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                onStop();
                finish();
            }
        };

        proximitySensorListener = new SensorEventListener() {
            boolean rep;

            //Tæller rygbøjninger
            @SuppressLint("SetTextI18n")
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
                textview.setText("Backbends:" + String.valueOf((backbendExercise.getReps() - 1))); //-1, fordi den starter på 1 af en eller anden grund.

                switch (backbendExercise.getReps()){
                    case 11:
                        haidokenSound.start();
                        break;
                    case 16:
                        bruhexplosionSound.start();
                        break;
                    case 21:
                        yesSound.start();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }
    @Override
    protected void onStop(){
        super.onStop();
        sensorManager.unregisterListener(proximitySensorListener);
    }
    }