package com.example.firebase;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.RequiresApi;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.DialogRedirect;


public class Pushups extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private SensorEventListener proximitySensorListener;
    CountDownTimer countDownTimer;
    CountDownTimer countDownTimerBefore;
    ExerciseData exerciseData;
    int millisInFuture;
    int countDownInterval;
    int i = 0;
    int delayMillis;


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushups);


        //actionbar hide
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        final TextView textview=(TextView) findViewById(R.id.textView);
        textview.setVisibility(View.INVISIBLE);
        final TextView pushupTimer = findViewById(R.id.pushupTimer);

        final PushupExercise pushupExercise = new PushupExercise(1); //Starter med nul reps
        final MediaPlayer haidokenSound = MediaPlayer.create(this, R.raw.haidoken); //Create sound
        final MediaPlayer bruhexplosionSound = MediaPlayer.create(this, R.raw.bruhexplosion); //Create sound
        final MediaPlayer yesSound = MediaPlayer.create(this, R.raw.yes); //Create sound
        exerciseData = ExerciseData.getInstance();

        Toast.makeText(Pushups.this,"Starting pushups in...",Toast.LENGTH_SHORT).show();

        if(proximitySensor == null){
            Toast.makeText(this, "Proximity sensor not available !", Toast.LENGTH_LONG).show();
            finish();
        }

        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        final Handler handler = new Handler();

        Button skipPushups = findViewById(R.id.skipPushups);
        skipPushups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countDownTimer.cancel();
                countDownTimerBefore.cancel();
                progressBar.setVisibility(View.INVISIBLE);
                ExerciseData exerciseData = ExerciseData.getInstance();
                exerciseData.addExercise(pushupExercise);
                Intent exercise2 = new Intent(Pushups.this, Squats.class);
                startActivity(exercise2);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
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

                pushupTimer.setText(millisUntilFinished/1000 + "");

            }

            @Override
            public void onFinish() {

                textview.setVisibility(View.VISIBLE);
                Toast.makeText(Pushups.this, "GO", Toast.LENGTH_SHORT).show();
                sensorManager.registerListener(proximitySensorListener, proximitySensor, 2*1000*1000, 1000);
                //onStop();
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




                pushupTimer.setText(millisUntilFinished/1000 + "");
            }

            @Override
            public void onFinish() {
                Toast.makeText(Pushups.this,"Get ready for Squats!",Toast.LENGTH_SHORT).show();
                exerciseData.addExercise(pushupExercise);
                Intent exercise2 = new Intent(Pushups.this, Squats.class);
                //exercise2.putExtra("PushupReps")
                startActivity(exercise2);
                progressBar.setVisibility(View.INVISIBLE);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                onStop();
                finish();
            }
        };




        proximitySensorListener = new SensorEventListener() {
            boolean rep;
            //int reps = 0;
            @SuppressLint("SetTextI18n")
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float currentValue = sensorEvent.values[0];

                if(currentValue == 5.0){
                    rep = false;
                }
                if (currentValue == 0.0 && rep == false){
                    //reps++;
                    pushupExercise.addRep();
                    rep = true;
                }
                //Bare fjerne sensorværdien, have et billede der ændrer sig, og et tal over. Timer under billedet, der måske kunne være rundt.
                textview.setText("Pushups: " + String.valueOf(pushupExercise.getReps()));
                switch (pushupExercise.getReps()){
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

        //sensorManager.registerListener(proximitySensorListener, proximitySensor, 2*1000*1000, 1000);
    }
    @Override
    protected void onStop(){
        super.onStop();
        sensorManager.unregisterListener(proximitySensorListener);
    }


}