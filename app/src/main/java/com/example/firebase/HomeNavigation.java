package com.example.firebase;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeNavigation extends AppCompatActivity implements View.OnClickListener{


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navigation);

        //actionbar hide
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //Skrive på harddisk, gemme hvem der er login. Kunne også gemme password i guess, for at tjekke hashcode og sådan.
        final SharedPreferences gemmeobjekt = PreferenceManager.getDefaultSharedPreferences(this);
        final String user = gemmeobjekt.getString("username", "");
        final TextView displayName = findViewById(R.id.welcomeDash);

        //Database
        final FirebaseDatabase[] database = {FirebaseDatabase.getInstance()}; //Get instance of database
        final DatabaseReference myRef = database[0].getReference("User"); //Get reference to certain spot in database, tror det er til når jeg prøvede at hente data. Også når jeg indsætter data.
        final DatabaseReference myRefComp = database[0].getReference("Competition");
        final User user1 = User.getInstance(this);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        displayName.setText("Welcome " + user + "!");

        //init and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set home selected

        bottomNavigationView.setSelectedItemId(R.id.home);

        //perform itemselectedlistener

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.home:
                        return true;

                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext()
                                , MagicPlace.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        return true;
                    case R.id.notifications:
                        startActivity(new Intent(getApplicationContext()
                                , Competition.class));
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                        //overridePendingTransition(0,0);
                        return true;


                }
                return false;
            }
        });

        initObjects();

    }

    private void initObjects(){

        CardView profileDash = findViewById(R.id.profileDash);
        CardView workoutDash = findViewById(R.id.workoutDash);
        CardView bmiDash = findViewById(R.id.bmiDash);
        //CardView planDash = findViewById(R.id.planDash);

        profileDash.setOnClickListener(this);
        workoutDash.setOnClickListener(this);
        bmiDash.setOnClickListener(this);
        //planDash.setOnClickListener(this);


    }

    //setting board clickable

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.profileDash:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeNavigation.this,Profile.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;


            case R.id.workoutDash:
                // Toast.makeText(this, "Workout", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeNavigation.this,Exercises.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;

            case R.id.bmiDash:
                Toast.makeText(this, "BMI Calculator", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(HomeNavigation.this,Insights.class));
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                break;

        }

    }
}