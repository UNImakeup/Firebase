package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class Squats extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor acceleroMeterSensor;
    private SensorEventListener acceleroSensorListener;
    TextView textview;
    final SquatExercise squatExercise = new SquatExercise(1);
    ExerciseData exerciseData;
    int millisInFuture;
    int countDownInterval;
    int delayMillis;
    int i = 0;
    MediaPlayer haidokenSound;
    MediaPlayer bruhexplosionSound;
    MediaPlayer yesSound;
    CountDownTimer countDownTimer;
    CountDownTimer countDownTimerBefore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_squat);


        //actionbar hide
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        final Handler handler = new Handler();
        final ProgressBar progressBar = findViewById(R.id.progressBarSquats);
        progressBar.setVisibility(View.INVISIBLE);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acceleroMeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        textview = (TextView) findViewById(R.id.textView69);
        textview.setVisibility(View.INVISIBLE);
        final TextView squatTimer = findViewById(R.id.squatTimer);

        haidokenSound = MediaPlayer.create(this, R.raw.haidoken); //Create sound
        bruhexplosionSound = MediaPlayer.create(this, R.raw.bruhexplosion); //Create sound
        yesSound = MediaPlayer.create(this, R.raw.yes); //Create sound

        exerciseData = ExerciseData.getInstance();

        Button skipSquats = findViewById(R.id.skipSquats);
        skipSquats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseData exerciseData = ExerciseData.getInstance();
                exerciseData.addExercise(squatExercise);
                Intent exercise3 = new Intent(Squats.this, Situp.class);
                startActivity(exercise3);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                countDownTimer.cancel();
                countDownTimerBefore.cancel();
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
                delayMillis = 180;
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
                squatTimer.setText(millisUntilFinished/1000 + "");
            }

            @Override
            public void onFinish() {

                Toast.makeText(Squats.this, "GO", Toast.LENGTH_SHORT).show();
                sensorManager.registerListener(Squats.this, acceleroMeterSensor,  sensorManager.SENSOR_DELAY_NORMAL);
                countDownTimer.start();
                progressBar.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(i<=100){

                            //progressText.setText(""+ i);
                            progressBar.setProgress(i);
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
                squatTimer.setText(millisUntilFinished/1000 + "");
                textview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                Toast.makeText(Squats.this,"Get ready for Situps!",Toast.LENGTH_SHORT).show();
                exerciseData.addExercise(squatExercise);
                Intent exercise3 = new Intent(Squats.this, Situp.class);
                startActivity(exercise3);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                onStop();
                finish();
            }
        };
    }

    @Override
    protected void onDestroy() {
        sensorManager.unregisterListener(acceleroSensorListener);
        super.onDestroy();
    }

    int reps = 0;
    boolean squat;
    double currentValue;
    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        currentValue = sensorEvent.values[1];
        if(currentValue < 1.0) {
            squat = true;
        }
        if(squat == true && currentValue > 8.0){
            squat =false;
            squatExercise.addRep();
        }
//Bare fjerne sensorværdien, have et billede der ændrer sig, og et tal over. Timer under billedet, der måske kunne være rundt.
        textview.setText("Squats: " + squatExercise.getReps());
        double lastValue = currentValue;

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
    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    @Override
    protected void onStop(){
        super.onStop();
        sensorManager.unregisterListener(Squats.this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }



}