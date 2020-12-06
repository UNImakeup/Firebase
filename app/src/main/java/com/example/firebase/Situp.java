package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_situp);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acceleroMeter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(Situp.this, acceleroMeter,  sensorManager.SENSOR_DELAY_NORMAL);
        textview = (TextView) findViewById(R.id.textView2);
        final TextView situpTimer = findViewById(R.id.situpTimer);

        CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                situpTimer.setText(millisUntilFinished/1000 + " Seconds left");
            }

            @Override
            public void onFinish() { //Kunne starte ny timer i stedet og ændre textviews, for at have pause før øvelsen begynder. Når den første timer er slut kunne man registerlistener.
                Toast.makeText(Situp.this,"finish",Toast.LENGTH_SHORT).show();
                ExerciseData exerciseData = ExerciseData.getInstance();
                exerciseData.addExercise(situpExercise);
                Intent exercise3 = new Intent(Situp.this, Backbends.class); //Putextra med sværhedsgrad, måske andet objekt med exercise, hvor vi gemmer reps.
                startActivity(exercise3);
                onStop();
                finish();
            }
        };

        Toast.makeText(Situp.this,"time start", Toast.LENGTH_SHORT).show(); //Kan måske fjernes. Man kan se den er gået i gang.
        countDownTimer.start(); //Skal måske rykkes ned til metoden?



    }

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