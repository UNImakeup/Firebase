package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class Notifications extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        //init and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //set home selected

        bottomNavigationView.setSelectedItemId(R.id.notifications);

        //perform itemselectedlistener

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){

                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext()
                                , Settings.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                , HomeNavigation.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.notifications:

                        return true;


                }
                return false;
            }
        });

        final FirebaseDatabase[] database = {FirebaseDatabase.getInstance()}; //Get instance of database
        final DatabaseReference myRefComp = database[0].getReference("Competition"); //Get reference to certain spot in database, tror det er til når jeg prøvede at hente data. Også når jeg indsætter data.
        final DatabaseReference myRefUser = database[0].getReference("User"); //Get reference to certain spot in database, tror det er til når jeg prøvede at hente data. Også når jeg indsætter data.


        //onButtonClick: //Ved tryk på join competition knap
        //Skal have skrevet i tekstfelt. Hvis child med det id eksisterer, så skriv deres navn ind og sig "you have now joined". Ellers sig wrong number.


        //onButtonClick: //Ved tryk på start Competition knap
        /*
        Random random = new Random();
        int randomCompNumber = random.nextInt(1000);
        if(Totalreps(på database).exists) {
            myRef.child(String.valueOf(randomCompNumber)).child(user.getUser).setValue(myRefUser.child(user.getUser).child(TotalReps));
        }
        setText("Exercise to win the Competition" +
                "\n Send the code to a friend, for them to join the Competition");
*/
        //Skal så bare gemme den nye totalReps under Comp. Så gemme datoen for upload der også. Se om det er mere end en uge siden, for så at slette hvis sandt.
        //For hver træning lægge en ny totalReps op på både egen bruger (all time), og comp, som slettes hvis der går over en uge. Kan måske også bare være fra hvornår man startede.
        //Hvis ens værdi så er lavere end modstanderens kan der stå i profil at man taber.
        //Man kan joine flere comps på samme tid.
        //Hvis vi har gemt det under brugeren kan det også vises på Profil med all time total Reps.

    }
}