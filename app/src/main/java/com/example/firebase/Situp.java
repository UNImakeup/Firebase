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


public class Situp extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor acceleroMeter;
    private SensorEventListener acceleroSensorListener;
    TextView textview;
    final SitupExercise situpExercise = new SitupExercise(1);
    ExerciseData exerciseData;
    int millisInFuture;
    int countDownInterval;
    int delayMillis;
    int i =0;
    CountDownTimer countDownTimer;
    CountDownTimer countDownTimerBefore;
    MediaPlayer haidokenSound;
    MediaPlayer bruhexplosionSound;
    MediaPlayer yesSound;




    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situp);


        //actionbar hide
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();


        final Handler handler = new Handler();
        final ProgressBar progressBarSitups = findViewById(R.id.progressBarSitups);
        progressBarSitups.setVisibility(View.INVISIBLE);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acceleroMeter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        textview = (TextView) findViewById(R.id.textView2);
        textview.setVisibility(View.INVISIBLE);
        final TextView situpTimer = findViewById(R.id.situpTimer);
        exerciseData = ExerciseData.getInstance();

        haidokenSound = MediaPlayer.create(this, R.raw.haidoken); //Create sound
        bruhexplosionSound = MediaPlayer.create(this, R.raw.bruhexplosion); //Create sound
        yesSound = MediaPlayer.create(this, R.raw.yes); //Create sound


        Button skipSitups = findViewById(R.id.skipSitups);
        skipSitups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseData exerciseData = ExerciseData.getInstance();
                exerciseData.addExercise(situpExercise);
                Intent exercise3 = new Intent(Situp.this, Backbends.class);
                startActivity(exercise3);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                progressBarSitups.setVisibility(View.INVISIBLE);
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
                delayMillis = 170;
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
                situpTimer.setText(millisUntilFinished/1000 + "");
            }

            @Override
            public void onFinish() {
                Toast.makeText(Situp.this, "GO", Toast.LENGTH_SHORT).show();
                sensorManager.registerListener(Situp.this, acceleroMeter,  sensorManager.SENSOR_DELAY_NORMAL);
                countDownTimer.start();
                progressBarSitups.setVisibility(View.VISIBLE);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(i<=100){

                            progressBarSitups.setProgress(i);
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
                situpTimer.setText(millisUntilFinished/1000 + "");
                textview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() { //Kunne starte ny timer i stedet og ændre textviews, for at have pause før øvelsen begynder. Når den første timer er slut kunne man registerlistener.
                Toast.makeText(Situp.this,"Get ready for Backbends!",Toast.LENGTH_SHORT).show();
                exerciseData.addExercise(situpExercise);
                Intent exercise3 = new Intent(Situp.this, Backbends.class); //Putextra med sværhedsgrad, måske andet objekt med exercise, hvor vi gemmer reps.
                startActivity(exercise3);
                progressBarSitups.setVisibility(View.INVISIBLE);
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

    boolean situp;
    double currentValue;
    @SuppressLint("SetTextI18n")
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        currentValue = sensorEvent.values[1];

        if(currentValue < 1.0) { //Kunne nok være i egen klasse, metode man kalder
            situp = true;
        }
        if(situp==true && currentValue > 4.0){
            situp=false;
            situpExercise.addRep();
        }
//Bare fjerne sensorværdien, have et billede der ændrer sig, og et tal over. Timer under billedet, der måske kunne være rundt.
        textview.setText("Situps: " + situpExercise.getReps());
        double lastValue = currentValue;

        switch (situpExercise.getReps()){
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
        sensorManager.unregisterListener(Situp.this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    }