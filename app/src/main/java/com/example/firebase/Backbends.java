package com.example.firebase;

import androidx.appcompat.app.ActionBar;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;


import org.w3c.dom.Text;

import java.util.Objects;


public class Backbends extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private SensorEventListener proximitySensorListener;
    CountDownTimer countDownTimer;
    CountDownTimer countDownTimer2;
    TextView backbendTimer;
    TextView backbendTimer2;
    TextView textview;
    Button skipBackbends;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backbends);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout1);

        //init sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //init reps
        textview=(TextView) findViewById(R.id.textView4);
        final BackbendExercise backbendExercise = new BackbendExercise(1);


        final MediaPlayer haidokenSound = MediaPlayer.create(this, R.raw.haidoken); //Create sound
        final MediaPlayer bruhexplosionSound = MediaPlayer.create(this, R.raw.bruhexplosion); //Create sound
        final MediaPlayer yesSound = MediaPlayer.create(this, R.raw.yes); //Create sound

        //init timer
        backbendTimer = findViewById(R.id.backBendTimer);
        backbendTimer2 = findViewById(R.id.backBendtimer2);

        //init skip button
        skipBackbends = findViewById(R.id.skipBackbends);
        skipBackbends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countDownTimer.cancel();
                countDownTimer2.cancel();
                onStop();
                ExerciseData exerciseData = ExerciseData.getInstance();
                exerciseData.addExercise(backbendExercise);
                Intent goHome = new Intent(Backbends.this, WorkoutDone.class);
                startActivity(goHome);
                onStop();
                finish();

            }
        });


        //implementing timer 1

        if(proximitySensor == null){
            Toast.makeText(this, "Proximity sensor not available !", Toast.LENGTH_LONG).show();
            finish();
        }

        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                backbendTimer.setText(millisUntilFinished/1000 + "");
                backbendTimer2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFinish() {
                Toast.makeText(Backbends.this, "GO", Toast.LENGTH_SHORT).show();
                backbendTimer.setText("");
                countDownTimer2.start();
                onStop();

            }
        };

        //implementin timer 2

        countDownTimer2 = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                backbendTimer2.setVisibility(View.VISIBLE);
                backbendTimer2.setText(millisUntilFinished/1000+"");

            }

            @Override
            public void onFinish() {

                Toast.makeText(Backbends.this,"Workout Done!",Toast.LENGTH_SHORT).show();
                ExerciseData exerciseData = ExerciseData.getInstance();
                exerciseData.addExercise(backbendExercise);
                Intent goHome = new Intent(Backbends.this, WorkoutDone.class);
                startActivity(goHome);
                onStop();
                finish();

            }
        };

        Toast.makeText(Backbends.this,"Get ready for Backbends!", Toast.LENGTH_SHORT).show();
        countDownTimer.start();

        //implementing sensor

        proximitySensorListener = new SensorEventListener() {
            //int reps = 0;
            boolean rep;

            //Fungerende med rygbøjninger.
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float currentValue = sensorEvent.values[0];
                System.out.println(currentValue); // Skal bare lige se hvad værdierne er


                if(currentValue == 0.0){
                    rep = false;
                }
                if (currentValue == 5.0 && rep == false){
                    //reps++;
                    backbendExercise.addRep();
                    rep = true;
                }
                //System.out.println(reps);
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
/*
                if ((reps - 1) == 2){
                    //onStop();
                    //Burde nok tilføje en slutskærm, hvor man viser reps og sådan
                    Intent workoutDone = new Intent(Backbends.this, HomeNavigation.class);
                    startActivity(workoutDone);
                    onStop();
                }

 */
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