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
import android.widget.Button;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;

import java.util.Objects;

public class Pushups extends AppCompatActivity {

    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private SensorEventListener proximitySensorListener;
    private CountDownTimer countDownTimer;
    private CountDownTimer countDownTimer2;
    private TextView pushupTimer;
    private TextView pushupsTimer2;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pushups);

        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_layout1);


        //init sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        //init Reps
        final TextView textview = (TextView) findViewById(R.id.textView);

       //init timer

        pushupTimer = findViewById(R.id.pushupTimer);
        pushupsTimer2 = findViewById(R.id.pushupsTimer2);


        //init reps

        final PushupExercise pushupExercise = new PushupExercise(1); //Starter med nul reps

       //init sound
        final MediaPlayer haidokenSound = MediaPlayer.create(this, R.raw.haidoken); //Create sound
        final MediaPlayer bruhexplosionSound = MediaPlayer.create(this, R.raw.bruhexplosion); //Create sound
        final MediaPlayer yesSound = MediaPlayer.create(this, R.raw.yes); //Create sound]

        //init skip Button
         Button skipPushups = findViewById(R.id.skipPushups);
         skipPushups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countDownTimer.cancel();
                countDownTimer2.cancel();
                onStop();
                ExerciseData exerciseData = ExerciseData.getInstance();
                exerciseData.addExercise(pushupExercise);
                Intent exercise2 = new Intent(Pushups.this, Squats.class);

                //exercise2.putExtra("PushupReps")
                startActivity(exercise2);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                finish();


            }
        });

        if(proximitySensor == null){
            Toast.makeText(this, "Proximity sensor not available!", Toast.LENGTH_LONG).show();
            finish();
        }


        //implementing timer 1

         countDownTimer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                pushupTimer.setText(millisUntilFinished/1000 + "");

            }

            @Override
            public void onFinish() {
                Toast.makeText(Pushups.this,"GO",Toast.LENGTH_SHORT).show();
               pushupTimer.setText("");
              countDownTimer2.start();
              onStop();

            }
        };

        //implementing timer 2

         countDownTimer2 = new CountDownTimer(30500,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                pushupsTimer2.setVisibility(View.VISIBLE);
                pushupsTimer2.setText(millisUntilFinished/1000+"");

            }

            @Override
            public void onFinish() {

                //Toast.makeText(Pushups.this,"Get ready for Squats!",Toast.LENGTH_SHORT).show();
                ExerciseData exerciseData = ExerciseData.getInstance();
                exerciseData.addExercise(pushupExercise);
                Intent exercise2 = new Intent(Pushups.this, Squats.class);

                //exercise2.putExtra("PushupReps")
                startActivity(exercise2);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                onStop();
                finish();
            }
        };

        //Toast.makeText(Pushups.this,"Get ready for Pushups!",Toast.LENGTH_SHORT).show();
        countDownTimer.start();


        //implementing sensor

        proximitySensorListener = new SensorEventListener() {
            //int reps = 0;
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                if (sensorEvent.values[0] < proximitySensor.getMaximumRange()){
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                    //reps++;
                    pushupExercise.addRep();
                } else {
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                }
                //System.out.println(reps);
                //Bare fjerne sensorværdien, have et billede der ændrer sig, og et tal over. Timer under billedet, der måske kunne være rundt.
                textview.setText(String.valueOf(pushupExercise.getReps()));
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

                /*
                if(reps == 2){
                    //onStop();
                    //onDestroy();

                    Intent exercise2 = new Intent(Pushups.this, Situp.class);
                    startActivity(exercise2);
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