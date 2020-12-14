package com.example.firebase;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MagicPlace extends AppCompatActivity {
    MediaPlayer zen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magic_place);

        //actionbar hide
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();


        //implementing bottom navigationBar

        //init and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set home selected

        bottomNavigationView.setSelectedItemId(R.id.magic_place);

        zen = MediaPlayer.create(this, R.raw.shiloh); //Create sound
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

                    case R.id.magic_place:
                        return true;

                    case R.id.home:
                        zen.stop();
                        zen.setLooping(false);
                        startActivity(new Intent(getApplicationContext()
                                , HomeNavigation.class));
                        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                        return true;

                    case R.id.competition:
                        zen.stop();
                        zen.setLooping(false);
                        startActivity(new Intent(getApplicationContext()
                                , Competition.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        return true;
                }
                return false;
            }
        });
    }
    @Override
    protected void onPause() {
        zen.stop();
        zen.setLooping(false);
        //super.onStop();
        super.onPause();
    }
}