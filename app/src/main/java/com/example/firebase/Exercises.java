package com.example.firebase;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Exercises extends AppCompatActivity implements View.OnClickListener {

    TextView t1;
    TextView exercisesHeader;
    Button b1, easyBtn, mediumBtn, hardBtn;
    CountDownTimer countDownTimer;
    ExerciseData exerciseData;
    MediaPlayer lyd; //Create sound
    TextView chooseDifficulty;
    CardView easyCard, hardCard, mediumCard;
    GridLayout gridlayoutexercises;
    BottomNavigationView bottomNavigationView;
    RelativeLayout relativeLayoutØverst;
    RelativeLayout relativeNederst;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);


        //actionbar hide
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();


        //init and assign variable
      bottomNavigationView = findViewById(R.id.bottom_navigation);
      easyCard = findViewById(R.id.easyCard);
      mediumCard = findViewById(R.id.mediumCard);
      hardCard = findViewById(R.id.hardCard);

      easyCard.setOnClickListener(this);
      mediumCard.setOnClickListener(this);
      hardCard.setOnClickListener(this);


        chooseDifficulty = findViewById(R.id.choosedifficulty);
        exercisesHeader = findViewById(R.id.textView5);
        gridlayoutexercises = findViewById(R.id.gridLayoutexercises);
        relativeNederst = findViewById(R.id.relativeLayoutnederst);
        relativeLayoutØverst = findViewById(R.id.relativeLayoutHeader);
        exerciseData = ExerciseData.getInstance();

        chooseDifficulty.setVisibility(View.VISIBLE);


        lyd = MediaPlayer.create(this, R.raw.ready_2); //Create sound
        lyd.start(); //Play sound


        //animeret bacggrund

        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();

        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        //implementing bottom navigationBar
        //perform itemselectedlistener

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                ,MainActivity.class));
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        return true;

                    case R.id.magic_place:
                        startActivity(new Intent(getApplicationContext()
                                , MagicPlace.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        return true;

                    case R.id.competition:
                        startActivity(new Intent(getApplicationContext()
                                , Competition.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        return true;


                }
                return false;
            }
        });


        //start timer
        t1 = findViewById(R.id.textView);
        t1.setVisibility(View.INVISIBLE);

        countDownTimer = new CountDownTimer(1500,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                t1.setText(millisUntilFinished/1000 + "");
            }


            @Override
            public void onFinish() {
                lyd.stop();
               // t1.setText("time finish");
              //  Toast.makeText(Exercises.this,"finish",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Exercises.this, Pushups.class); //Kan sige putExtra med sværhedsgraden.
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();

            }


        };



    }

    @Override
    protected void onPause() {
        lyd.stop();
        //super.onStop();
        super.onPause();
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.easyCard:
                Toast.makeText(Exercises.this,"Get ready for your program!",Toast.LENGTH_SHORT).show();
                t1.setVisibility(View.VISIBLE);
                countDownTimer.start();
                exerciseData.setDifficulty(1);
                exercisesHeader.setVisibility(View.INVISIBLE);
                chooseDifficulty.setVisibility(View.INVISIBLE);
                gridlayoutexercises.setVisibility(View.INVISIBLE);
                bottomNavigationView.setVisibility(View.INVISIBLE);
                relativeLayoutØverst.setVisibility(View.INVISIBLE);
                relativeNederst.setVisibility(View.INVISIBLE);



            break;


            case R.id.mediumCard:
                Toast.makeText(Exercises.this,"Get ready for your program!",Toast.LENGTH_SHORT).show();
                countDownTimer.start();
                exerciseData.setDifficulty(2);
                chooseDifficulty.setVisibility(View.INVISIBLE);
                exercisesHeader.setVisibility(View.INVISIBLE);
                gridlayoutexercises.setVisibility(View.INVISIBLE);
                bottomNavigationView.setVisibility(View.INVISIBLE);
                t1.setVisibility(View.VISIBLE);
                relativeLayoutØverst.setVisibility(View.INVISIBLE);
                relativeNederst.setVisibility(View.INVISIBLE);
            break;


            case R.id.hardCard:
                Toast.makeText(Exercises.this,"Get ready for your program!",Toast.LENGTH_SHORT).show();
                countDownTimer.start();
                exerciseData.setDifficulty(3);
                chooseDifficulty.setVisibility(View.INVISIBLE);
                exercisesHeader.setVisibility(View.INVISIBLE);
                gridlayoutexercises.setVisibility(View.INVISIBLE);
                bottomNavigationView.setVisibility(View.INVISIBLE);
                t1.setVisibility(View.VISIBLE);
                relativeLayoutØverst.setVisibility(View.INVISIBLE);
                relativeNederst.setVisibility(View.INVISIBLE);
                break;



        }



    }
}