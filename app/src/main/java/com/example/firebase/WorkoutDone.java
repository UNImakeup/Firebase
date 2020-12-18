package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WorkoutDone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_done);
        TextView workoutSummary = findViewById(R.id.workoutResult);
        Button workoutDoneBtn = findViewById(R.id.workoutDoneBtn);
        final MediaPlayer lyd = MediaPlayer.create(this, R.raw.wow); //Create sound
        final FirebaseDatabase[] database = {FirebaseDatabase.getInstance()}; //Get instance of database
        final DatabaseReference myRefUser = database[0].getReference("User"); //Get reference to certain spot in database, tror det er til når jeg prøvede at hente data. Også når jeg indsætter data.
        final DatabaseReference myRefComp = database[0].getReference("Competition");
        final ExerciseData exerciseData = ExerciseData.getInstance(); //Hent exerciseData så vi kan printe resultater
        final User user = User.getInstance(this);

        //Spille anime wow lyd
        lyd.start();


        //vise tekst. Måske bruge metode i ExerciseData, print og sådan. Vise getSum til sidst.
        workoutSummary.setText("Pushups: " + exerciseData.getExercises().get(0).getReps() +
        "\n Squats: " + exerciseData.getExercises().get(1).getReps() +
                "\n Situps: " + exerciseData.getExercises().get(2).getReps() +
                "\n Backbends: " + (exerciseData.getExercises().get(3).getReps() - 1) +
                "\n\n Total Reps: " + exerciseData.getSum()
        );


        //Mangler bare at kunne hente fra bruger objektet.Kan vi nu
        //Her lægger vi reps op på bruger i  databasen
        myRefUser.addListenerForSingleValueEvent(new ValueEventListener() {
            int totalReps = 0;
            int competitionID;
            int userCompetitionID;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(user.getUser()).child("TotalReps").exists()){
                    totalReps +=  dataSnapshot.child(user.getUser()).child("TotalReps").getValue(Integer.class); //add current totalreps.//Virker lige nu første gang, prøver at få det til at virke nå den nuværende værdi skal oveni. Tror værdierne fucker. Det var det, nu virker det. https://stackoverflow.com/questions/55042570/cast-datasnapshot-from-firebase-to-integer-failed
                //Det virker nu, men kommentarer viser tankeprocess.
                }

                totalReps += exerciseData.getSum();
                myRefUser.child(user.getUser()).child("TotalReps").setValue(totalReps); //For at sætte totalreps.

                if(dataSnapshot.child(user.getUser()).child("CompetitionID").exists()) {
                    competitionID =  dataSnapshot.child(user.getUser()).child("CompetitionID").getValue(Integer.class);
                    user.setCompetitionID(competitionID);
                    userCompetitionID = Integer.parseInt(dataSnapshot.child(user.getUser()).child("CompetitionID" + competitionID).child(user.getUser() + "UserValue").getValue(String.class));
                    user.setUserCompetitionID(userCompetitionID);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myRefComp.addListenerForSingleValueEvent(new ValueEventListener() {
            int compReps = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(String.valueOf(user.getCompetitionID())).exists())//Skal tjekke i comp, skal lige finde ud af hvordan. Tror ikke man kan lave listener herinde)
                    //Det kan man ikke. Enten lave databasestruktur om eller lægge CompID i sharedPreferences og hente derfra.
                    // Kunne vel egentlig også bare være i brugerobjekt. Gøre det herinde, før man lægger op.
                    if(dataSnapshot.child(String.valueOf(user.getCompetitionID())).child(String.valueOf(user.getUserCompetitionID())).child("CompReps").exists()) {
                        compReps += Integer.parseInt(dataSnapshot.child(String.valueOf(user.getCompetitionID())).child(String.valueOf(user.getUserCompetitionID())).child("CompReps").getValue(String.class));
                    }
                compReps += exerciseData.getSum();
                    myRefComp.child(String.valueOf(user.getCompetitionID())).child(String.valueOf(user.getUserCompetitionID())).child("CompReps").setValue(String.valueOf(compReps)); //Dette er kommet i DatabaseSingleton. Skal bare kalde den her.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Knap til homeNavigation
        workoutDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exerciseData.clearExercises(); //For at cleare øvelserne, så man kan træne igen.
                Intent goHome = new Intent(WorkoutDone.this, HomeNavigation.class);
                startActivity(goHome);
            }
        });

    }
}