package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;


public class Squats extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor acceleroMeterSensor;
    private SensorEventListener acceleroSensorListener;
    TextView textview;
    final SquatExercise squatExercise = new SquatExercise(1);
    /*
    final MediaPlayer haidokenSound = MediaPlayer.create(this, R.raw.haidoken); //Create sound
    final MediaPlayer bruhexplosionSound = MediaPlayer.create(this, R.raw.bruhexplosion); //Create sound
    final MediaPlayer yesSound = MediaPlayer.create(this, R.raw.yes); //Create sound
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squat);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acceleroMeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(Squats.this, acceleroMeterSensor,  sensorManager.SENSOR_DELAY_NORMAL);
        textview = (TextView) findViewById(R.id.textView69);
        final TextView squatTimer = findViewById(R.id.squatTimer);



        CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                squatTimer.setText(millisUntilFinished/1000 + " Seconds left");
            }

            @Override
            public void onFinish() {
                Toast.makeText(Squats.this,"finish",Toast.LENGTH_SHORT).show();
                ExerciseData exerciseData = ExerciseData.getInstance();
                exerciseData.addExercise(squatExercise);
                Intent exercise3 = new Intent(Squats.this, Situp.class);
                startActivity(exercise3);
                onStop();
                finish();
            }
        };

        Toast.makeText(Squats.this,"time start", Toast.LENGTH_SHORT).show();
        countDownTimer.start(); //Skal måske rykkes ned til metoden?




    }

    @Override
    protected void onDestroy() {
        sensorManager.unregisterListener(acceleroSensorListener);
        super.onDestroy();
    }

    int reps = 0;
    boolean squat;
    double currentValue;
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        currentValue = sensorEvent.values[1];

        //i++;
        //System.out.println(i);

        //for(int i = 0; reps < 10; i++){
        if(/*sensorEvent.values[1] > 0 lastValue < 0.0 && */currentValue < 1.0) {
            squat = true;
            //int a = 1;
        }
        if(squat == true && currentValue > 8.0){
            //reps++;
            squat =false;
            reps++;
            squatExercise.addRep();
        }
//Bare fjerne sensorværdien, have et billede der ændrer sig, og et tal over. Timer under billedet, der måske kunne være rundt.
        textview.setText("Squats: " + squatExercise.getReps());
        double lastValue = currentValue;

        /*
        switch (squatExercise.getReps()){
            case 10:
                haidokenSound.start();
                break;
            case 15:
                bruhexplosionSound.start();
                break;
            case 20:
                yesSound.start();
        }

         */

/*
        if(reps == 2){
            //onStop();
            //onDestroy();
            Intent exercise3 = new Intent(Squats.this, Situp.class);
            startActivity(exercise3);
            onStop();
        }

 */
    }

    //System.out.println(sensorEvent.values[1]);

    //}

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    @Override
    protected void onStop(){
        super.onStop();
        sensorManager.unregisterListener(Squats.this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }



}