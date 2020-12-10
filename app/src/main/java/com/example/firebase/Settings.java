package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //implementing bottom navigationBar

        //init and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set home selected

        bottomNavigationView.setSelectedItemId(R.id.settings);

        final MediaPlayer zen = MediaPlayer.create(this, R.raw.shiloh); //Create sound
        zen.start();
        zen.setLooping(true); //Play sound

        ConstraintLayout constraintLayout = findViewById(R.id.activity_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();

        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();


        //perform itemselectedlistener

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.settings:
                        return true;

                    case R.id.home:
                        zen.stop();
                        zen.setLooping(false);
                        startActivity(new Intent(getApplicationContext()
                                , HomeNavigation.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.notifications:
                        zen.stop();
                        zen.setLooping(false);
                        startActivity(new Intent(getApplicationContext()
                                , Notifications.class));
                        overridePendingTransition(0,0);
                        return true;


                }
                return false;
            }
        });



    }
}