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


public class Situp extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor acceleroMeter;
    private SensorEventListener acceleroSensorListener;
    TextView textview;
    final SitupExercise situpExercise = new SitupExercise(1);
    ExerciseData exerciseData;
    int millisInFuture;
    int countDownInterval;
    CountDownTimer countDownTimer;
    CountDownTimer countDownTimerBefore;
    MediaPlayer haidokenSound;
    MediaPlayer bruhexplosionSound;
    MediaPlayer yesSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situp);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acceleroMeter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        textview = (TextView) findViewById(R.id.textView2);
        final TextView situpTimer = findViewById(R.id.situpTimer);
        exerciseData = ExerciseData.getInstance();

        haidokenSound = MediaPlayer.create(this, R.raw.haidoken); //Create sound
        bruhexplosionSound = MediaPlayer.create(this, R.raw.bruhexplosion); //Create sound
        yesSound = MediaPlayer.create(this, R.raw.yes); //Create sound


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
                situpTimer.setText(millisUntilFinished/1000 + "");
            }

            @Override
            public void onFinish() {
                Toast.makeText(Situp.this, "GO", Toast.LENGTH_SHORT).show();
                sensorManager.registerListener(Situp.this, acceleroMeter,  sensorManager.SENSOR_DELAY_NORMAL);
                countDownTimer.start();
            }
        };
        countDownTimerBefore.start();

        countDownTimer = new CountDownTimer(millisInFuture, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                situpTimer.setText(millisUntilFinished/1000 + " Seconds left");
            }

            @Override
            public void onFinish() { //Kunne starte ny timer i stedet og ændre textviews, for at have pause før øvelsen begynder. Når den første timer er slut kunne man registerlistener.
                Toast.makeText(Situp.this,"finish",Toast.LENGTH_SHORT).show();
                exerciseData.addExercise(situpExercise);
                Intent exercise3 = new Intent(Situp.this, Backbends.class); //Putextra med sværhedsgrad, måske andet objekt med exercise, hvor vi gemmer reps.
                startActivity(exercise3);
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