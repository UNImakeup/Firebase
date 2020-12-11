package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Exercises extends AppCompatActivity {
    TextView t1;
    Button b1, easyBtn, mediumBtn, hardBtn;
    CountDownTimer countDownTimer;
    ExerciseData exerciseData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);
        //implementing bottom navigationBar

        //init and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        easyBtn = findViewById(R.id.easyBtn);
        mediumBtn = findViewById(R.id.mediumBtn);
        hardBtn = findViewById(R.id.hardBtn);
        exerciseData = ExerciseData.getInstance();


        final MediaPlayer lyd = MediaPlayer.create(this, R.raw.ready_2); //Create sound
        lyd.start(); //Play sound




//Skal lige kigge på det her hjemme.

        RelativeLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();

        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();






        //perform itemselectedlistener

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                ,MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext()
                                , Settings.class));
                        overridePendingTransition(0,0);
                        return true;



                    case R.id.notifications:
                        startActivity(new Intent(getApplicationContext()
                                ,Notifications.class));
                        overridePendingTransition(0,0);
                        return true;


                }
                return false;
            }
        });


        //start timer
        t1 = findViewById(R.id.textView);

        countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                t1.setText(millisUntilFinished/1000 + "sec left");
            }



            @Override
            public void onFinish() {

                t1.setText("time finish");
                Toast.makeText(Exercises.this,"finish",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Exercises.this, Pushups.class); //Kan sige putExtra med sværhedsgraden.
                startActivity(intent);
                finish();

            }


        };

        easyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Exercises.this,"time start", Toast.LENGTH_SHORT).show();
                countDownTimer.start();
                exerciseData.setDifficulty(1);
            }
        });

        mediumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Exercises.this,"time start", Toast.LENGTH_SHORT).show();
                countDownTimer.start();
                exerciseData.setDifficulty(2);
            }
        });

        hardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Exercises.this,"time start", Toast.LENGTH_SHORT).show();
                countDownTimer.start();
                exerciseData.setDifficulty(3);
            }
        });


    }
}