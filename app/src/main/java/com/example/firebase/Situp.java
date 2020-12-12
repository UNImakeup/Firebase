package com.example.firebase;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.Objects;


public class Situp extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor acceleroMeter;
    private SensorEventListener acceleroSensorListener;
    final SitupExercise situpExercise = new SitupExercise(1);
    CountDownTimer countDownTimer;
    CountDownTimer countDownTimer2;
    TextView situpTimer;
    TextView situpTimer2;
    TextView textview;
    /*
    final MediaPlayer haidokenSound = MediaPlayer.create(this, R.raw.haidoken); //Create sound
    final MediaPlayer bruhexplosionSound = MediaPlayer.create(this, R.raw.bruhexplosion); //Create sound
    final MediaPlayer yesSound = MediaPlayer.create(this, R.raw.yes); //Create sound
     */


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situp);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout1);

        //init sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acceleroMeter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(Situp.this, acceleroMeter,  sensorManager.SENSOR_DELAY_NORMAL);
        textview = (TextView) findViewById(R.id.textView2);

        //init timers
        situpTimer = findViewById(R.id.situpTimer);
        situpTimer2 = findViewById(R.id.situpTimer2);

        //Button

        Button skipSitups = findViewById(R.id.skipSitups);
        skipSitups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseData exerciseData = ExerciseData.getInstance();
                exerciseData.addExercise(situpExercise);
                Intent exercise3 = new Intent(Situp.this, Backbends.class);
                startActivity(exercise3);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                countDownTimer.cancel();
                countDownTimer2.cancel();
                onStop();
                finish();

            }
        });

        //implementing timer 1

        countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                situpTimer.setText(millisUntilFinished/1000 + "");
                situpTimer2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFinish() { //Kunne starte ny timer i stedet og ændre textviews, for at have pause før øvelsen begynder. Når den første timer er slut kunne man registerlistener.
                Toast.makeText(Situp.this,"GO!",Toast.LENGTH_SHORT).show();
                situpTimer.setText("");
                countDownTimer2.start();
                onStop();


            }
        };


        //implementing timer2
        countDownTimer2 = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                situpTimer2.setVisibility(View.VISIBLE);
                situpTimer.setText(millisUntilFinished/1000+"");

            }

            @Override
            public void onFinish() {

                Toast.makeText(Situp.this,"Get ready for Backbends!",Toast.LENGTH_SHORT).show();
                ExerciseData exerciseData = ExerciseData.getInstance();
                exerciseData.addExercise(situpExercise);
                Intent exercise3 = new Intent(Situp.this, Backbends.class);
                startActivity(exercise3);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                onStop();
                finish();

            }
        };

        Toast.makeText(Situp.this,"Get ready for Situps!", Toast.LENGTH_SHORT).show(); //Kan måske fjernes. Man kan se den er gået i gang.
        countDownTimer.start();
    }

    //imoplementing sensor

    @Override
    protected void onDestroy() {
        sensorManager.unregisterListener(acceleroSensorListener);
        super.onDestroy();
    }

    int reps = 0;
    boolean situp;
    double currentValue;
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        currentValue = sensorEvent.values[1];

        //i++;
        //System.out.println(i);

        //for(int i = 0; reps < 10; i++){
        if(/*sensorEvent.values[1] > 0 lastValue < 0.0 && */currentValue < 1.0) { //Kunne nok være i egen klasse, metode man kalder
            situp = true;
            //int a = 1;
        }
        if(situp==true && currentValue > 4.0){
            //reps++;
            situp=false;
            reps++;
            situpExercise.addRep();
        }
//Bare fjerne sensorværdien, have et billede der ændrer sig, og et tal over. Timer under billedet, der måske kunne være rundt.
        textview.setText("Situps: " + situpExercise.getReps());
        double lastValue = currentValue;

        /*
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
         */

        /*
        if(reps == 2){
            //onStop();
            //onDestroy();
            Intent exercise3 = new Intent(Situp.this, Backbends.class);
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
        sensorManager.unregisterListener(Situp.this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }

    }